package com.czeta.onlinejudge.utils.utils;

import com.czeta.onlinejudge.utils.enums.IBaseStatusMsg;
import com.czeta.onlinejudge.utils.exception.APIRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @ClassName UploadUtils
 * @Description 文件上传工具类
 * @Author chenlongjie
 * @Date 2020/3/10 10:48
 * @Version 1.0
 */
@Slf4j
public final class UploadUtils {

    /**
     * 上传文件，默认文件名格式，yyyyMMddHHmmssS
     *
     * @param uploadPath
     * @param multipartFile
     * @return
     * @throws Exception
     */
    public static String upload(String uploadPath, MultipartFile multipartFile
            , List<String> allowUploadFileExtensions) throws Exception {
        return upload(uploadPath, multipartFile, new DefaultUploadFileNameHandleImpl(), allowUploadFileExtensions);
    }

    /**
     * 上传文件
     *
     * @param uploadPath           上传目录
     * @param multipartFile        上传文件
     * @param uploadFileNameHandle 回调
     * @param allowUploadFileExtensions 允许上传的后缀
     * @return
     * @throws Exception
     */
    public static String upload(String uploadPath, MultipartFile multipartFile, UploadFileNameHandle uploadFileNameHandle
            , List<String> allowUploadFileExtensions) throws Exception {
        String originalFilename = multipartFile.getOriginalFilename();
        String fileExtension = FilenameUtils.getExtension(originalFilename);
        AssertUtils.isTrue(allowUploadFileExtensions.contains(fileExtension), IBaseStatusMsg.APIEnum.FAILED, "上传文件类型不合法");
        // 获取输入流
        InputStream inputStream = multipartFile.getInputStream();
        // 文件保存目录
        File saveDir = new File(uploadPath);
        // 判断目录是否存在，不存在，则创建，如创建失败，则抛出异常
        if (!saveDir.exists()) {
            boolean flag = saveDir.mkdirs();
            if (!flag) {
                log.error("上传文件中创建{}目录失败", saveDir);
                throw new APIRuntimeException(IBaseStatusMsg.APIEnum.FAILED);
            }
        }
        String saveFileName;
        if (uploadFileNameHandle == null) {
            saveFileName = new DefaultUploadFileNameHandleImpl().handle(originalFilename);
        } else {
            saveFileName = uploadFileNameHandle.handle(originalFilename);
        }
        File saveFile = new File(saveDir, saveFileName);
        // 保存文件到服务器指定路径
        FileUtils.copyToFile(inputStream, saveFile);
        return saveFileName;
    }

    /**
     * 删除删除的文件
     *
     * @param uploadPath
     * @param saveFileName
     */
    public static void deleteQuietly(String uploadPath, String saveFileName) {
        File saveDir = new File(uploadPath);
        File saveFile = new File(saveDir, saveFileName);
        FileUtils.deleteQuietly(saveFile);

    }


    public static interface UploadFileNameHandle {
        /**
         * 回调处理接口
         *
         * @param originalFilename
         * @return
         */
        String handle(String originalFilename);
    }

    public static class DefaultUploadFileNameHandleImpl implements UploadFileNameHandle {

        @Override
        public String handle(String originalFilename) {
            // 文件后缀
            String fileExtension = FilenameUtils.getExtension(originalFilename);
            // 这里可自定义文件名称，比如按照业务类型/文件格式/日期
            // 此处按照文件日期存储
            String dateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssS"));
            String fileName = dateString + "." + fileExtension;
            return fileName;
        }
    }

}
