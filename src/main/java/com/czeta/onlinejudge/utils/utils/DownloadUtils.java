package com.czeta.onlinejudge.utils.utils;

import com.czeta.onlinejudge.utils.enums.IBaseStatusMsg;
import com.czeta.onlinejudge.utils.exception.APIRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.Base64Utils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

/**
 * @ClassName DownloadUtils
 * @Description 文件下载工具类
 * @Author chenlongjie
 * @Date 2020/3/10 13:33
 * @Version 1.0
 */
@Slf4j
public class DownloadUtils {

    /**
     * 下载文件，使用默认下载处理器
     * @param downloadDir
     * @param downloadFileName
     * @param allowFileExtensions
     * @param response
     * @throws Exception
     */
    public static void download(String downloadDir, String downloadFileName, List<String> allowFileExtensions, HttpServletResponse response) throws Exception{
        download(downloadDir,downloadFileName,allowFileExtensions,response,new DefaultDownloadHandler());
    }

    /**
     * 下载文件，使用自定义下载处理器
     * @param downloadDir 文件目录
     * @param downloadFileName 文件名称
     * @throws Exception
     */
    public static void download(String downloadDir, String downloadFileName, List<String> allowFileExtensions, HttpServletResponse response,DownloadHandler downloadHandler) throws Exception {
        AssertUtils.notBlank(downloadDir, IBaseStatusMsg.APIEnum.FAILED);
        AssertUtils.notBlank(downloadFileName, IBaseStatusMsg.APIEnum.FAILED);
        // 安全判断，防止../情况,防止出现类似非法文件名称：../../hello/123.txt
        if (downloadFileName.contains("..") || downloadFileName.contains("../")){
            throw new APIRuntimeException(IBaseStatusMsg.APIEnum.FAILED, "非法文件名称");
        }
        String fileExtension = FilenameUtils.getExtension(downloadFileName);
        AssertUtils.isTrue(allowFileExtensions.contains(fileExtension), IBaseStatusMsg.APIEnum.FAILED, "非法文件后缀");
        // 从服务器读取文件，然后输出
        File downloadFile = new File(downloadDir, downloadFileName);
        AssertUtils.isTrue(downloadFile.exists(), IBaseStatusMsg.APIEnum.FAILED, "文件不存在");
        // 判断文件类型，输出对应ContentType,如果没有对应的内容类型，可在config/mime-type.properties配置
        String contentType = ContentTypeUtils.getContentType(downloadFile);
        // 文件大小
        long length = downloadFile.length();
        // 下载回调处理
        if (downloadHandler == null){
            // 使用默认下载处理器
            downloadHandler = new DefaultDownloadHandler();
        }
        boolean flag = downloadHandler.handle(downloadDir, downloadFileName, downloadFile, fileExtension, contentType, length);
        if (!flag){
            log.info("下载自定义校验失败，取消下载");
            return;
        }

        // 下载文件名称编码，Firefox中文乱码处理
        String encodeDownFileName;
        HttpServletRequest request =  ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String browser = BrowserUtils.getCurrent(request);
        if(BrowserUtils.FIREFOX.equals(browser)) {
            encodeDownFileName = "=?UTF-8?B?" + (new String(Base64Utils.encodeToString(downloadFileName.getBytes("UTF-8")))) + "?=";
        } else {
            encodeDownFileName = URLEncoder.encode(downloadFileName,"utf-8").replaceAll("\\+", "%20");
        }
        log.info("下载文件：" + downloadFile.getAbsolutePath());

        response.reset();
        // 设置Content-Disposition响应头
        response.setHeader("Content-Disposition", "attachment;fileName=\"" + encodeDownFileName + "\"");
        // 设置响应Content-Type
        response.setContentType(contentType);
        // 设置响应文件大小
        response.setContentLengthLong(length);
        // 文件下载
        InputStream in = new BufferedInputStream(new FileInputStream(downloadFile));
        FileCopyUtils.copy(in, response.getOutputStream());
    }

    public static interface DownloadHandler{
        boolean handle(String dir, String fileName,File file,String fileExtension,String contentType,long length) throws Exception;
    }
    public static class DefaultDownloadHandler implements DownloadHandler{
        @Override
        public boolean handle(String dir, String fileName, File file, String fileExtension, String contentType, long length) throws Exception {
            return false;
        }
    }

}
