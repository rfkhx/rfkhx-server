package edu.upc.mishuserver.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.ClientAbortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.upc.mishuserver.dto.AppBinary;
import edu.upc.mishuserver.repositories.AppBinaryRepository;
import edu.upc.mishuserver.utils.StringConfigUtil;
import edu.upc.mishuserver.vo.UpdateInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * ReleaseController
 */
@Controller
@Slf4j
public class ReleaseController {

	@Autowired
	private AppBinaryRepository appBinaryRepository;

	@RequestMapping(value = "/down/{platform}/{filename}")
	public void downloadFile(@PathVariable(value = "filename", required = true) String f_name,
			@PathVariable(value = "platform", required = true) String platform, HttpServletRequest request,
			HttpServletResponse response, @RequestHeader(required = false) String range)
			throws UnsupportedEncodingException {
		String upPath = StringConfigUtil.getConfig("uppath");
		// 防止用户下载目录以外的文件
		String regEx = "[\\\\\\\\/:*?\\\"<>|]";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(f_name);

		if (matcher.matches()) {
			log.warn("IP为{}的用户尝试下载文件{}被拒绝。", request.getRemoteAddr(), f_name);
			return;
		}
		// 文件目录
		File file = new File(upPath + f_name);

		// 开始下载位置
		long startByte = 0;
		// 结束下载位置
		long endByte = file.length() - 1;

		// 有range的话
		if (range != null && range.contains("bytes=") && range.contains("-")) {
			range = range.substring(range.lastIndexOf("=") + 1).trim();
			String ranges[] = range.split("-");
			try {
				// 判断range的类型
				if (ranges.length == 1) {
					// 类型一：bytes=-2343
					if (range.startsWith("-")) {
						endByte = Long.parseLong(ranges[0]);
					}
					// 类型二：bytes=2343-
					else if (range.endsWith("-")) {
						startByte = Long.parseLong(ranges[0]);
					}
				}
				// 类型三：bytes=22-2343
				else if (ranges.length == 2) {
					startByte = Long.parseLong(ranges[0]);
					endByte = Long.parseLong(ranges[1]);
				}

			} catch (NumberFormatException e) {
				startByte = 0;
				endByte = file.length() - 1;
			}
		}
		// 要下载的长度（为啥要加一问小学数学老师去）
		long contentLength = endByte - startByte + 1;
		// 文件名
		String fileName = file.getName();
		// 文件类型
		// String contentType = request.getServletContext().getMimeType(fileName);

		// 各种响应头设置
		// 参考资料：https://www.ibm.com/developerworks/cn/java/joy-down/index.html
		// 坑爹地方一：看代码
		response.setHeader("Accept-Ranges", "bytes");

		// 配置文件下载
		response.setHeader("content-type", "application/octet-stream");
		response.setContentType("application/octet-stream");
		// 下载文件能正常显示中文
		try {
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			//
			e1.printStackTrace();
		}

		response.setHeader("Content-Length", String.valueOf(contentLength));
		// 坑爹地方三：Content-Range，格式为
		// [要下载的开始位置]-[结束位置]/[文件总大小]
		response.setHeader("Content-Range", "bytes " + startByte + "-" + endByte + "/" + file.length());

		BufferedOutputStream outputStream = null;
		RandomAccessFile randomAccessFile = null;
		// 已传送数据大小
		long transmitted = 0;
		try {
			randomAccessFile = new RandomAccessFile(file, "r");
			outputStream = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[4096];
			int len = 0;
			randomAccessFile.seek(startByte);
			// 坑爹地方四：判断是否到了最后不足4096（buff的length）个byte这个逻辑（(transmitted + len) <=
			// contentLength）要放前面！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
			// 不然会会先读取randomAccessFile，造成后面读取位置出错，找了一天才发现问题所在
			while ((transmitted + len) <= contentLength && (len = randomAccessFile.read(buff)) != -1) {
				outputStream.write(buff, 0, len);
				transmitted += len;
				// 停一下，方便测试，用的时候删了就行了
				Thread.sleep(10);
			}
			// 处理不足buff.length部分
			if (transmitted < contentLength) {
				len = randomAccessFile.read(buff, 0, (int) (contentLength - transmitted));
				outputStream.write(buff, 0, len);
				transmitted += len;
			}

			outputStream.flush();
			response.flushBuffer();
			randomAccessFile.close();
			System.out.println("下载完毕：" + startByte + "-" + endByte + "：" + transmitted);

		} catch (ClientAbortException e) {
			System.out.println("用户停止下载：" + startByte + "-" + endByte + "：" + transmitted);
			// 捕获此异常表示拥护停止下载
		} catch (IOException e) {
			log.error("用户请求下载文件{}时遇到读写异常{}", f_name, e.toString());
		} catch (InterruptedException e) {
			// e.printStackTrace();
			log.error("用户请求下载文件{}被中断：{}", f_name, e.toString());
		} finally {
			try {
				if (randomAccessFile != null) {
					randomAccessFile.close();
				}
			} catch (IOException e) {
				// e.printStackTrace();
				log.debug("关闭随机文件{}失败：{}", f_name, e.toString());
			}
		}
	}

	@RequestMapping("/update/{platform}")
	@ResponseBody
	UpdateInfo getUpdateInfo(HttpServletRequest request,@PathVariable String platform) {

	
		AppBinary appBinary = appBinaryRepository.findFirstByPlatformOrderByVersioncodeDesc(platform);
		UpdateInfo updateInfo;
		if (appBinary == null) {
			updateInfo = UpdateInfo.builder().code(1L).msg("没有找到该系统的发布版本！").build();
		} else {
			updateInfo = UpdateInfo.builder().code(0L).updateStatus(1).versionCode(appBinary.getVersioncode())
					.versionName(appBinary.getVersionname()).modifyContent(appBinary.getDescription())
					.downloadUrl(StringConfigUtil.getConfig("url", "http://loaclhost")+"/down/" + appBinary.getPlatform() + "/" + appBinary.getFilename())
					.apkSize(appBinary.getSize() / 1024).apkMd5(appBinary.getMd5()).build();
		}
		return updateInfo;
	}
}