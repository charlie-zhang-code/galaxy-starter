package io.charlie.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Charlie Zhang
 * @version v1.0
 * @date 29/05/2025
 * @description 图片裁剪工具类
 */
public class ImageCropper {

    /**
     * 裁剪方式枚举
     */
    public enum CropType {
        CENTER,    // 从中心裁剪
        TOP,       // 从顶部裁剪
        BOTTOM,    // 从底部裁剪
        LEFT,      // 从左侧裁剪
        RIGHT      // 从右侧裁剪
    }

    /**
     * 裁剪图片到指定比例
     *
     * @param file         原始图片文件
     * @param targetRatioW 目标比例宽度部分
     * @param targetRatioH 目标比例高度部分
     * @param cropType     裁剪方式枚举
     * @return 裁剪后的图片文件
     * @throws IOException 如果图片处理出错
     */
    public static MultipartFile cropImageToRatio(MultipartFile file,
                                                 int targetRatioW,
                                                 int targetRatioH,
                                                 CropType cropType) throws IOException {
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        // 计算目标尺寸
        int targetWidth, targetHeight;
        if ((double) width / height > (double) targetRatioW / targetRatioH) {
            // 原图更宽，以高度为基准计算宽度
            targetHeight = height;
            targetWidth = (int) (height * (double) targetRatioW / targetRatioH);
        } else {
            // 原图更高，以宽度为基准计算高度
            targetWidth = width;
            targetHeight = (int) (width * (double) targetRatioH / targetRatioW);
        }

        // 计算裁剪区域
        int x = 0, y = 0;
        if (width > targetWidth) {
            switch (cropType) {
                case TOP:
                    x = (width - targetWidth) / 2;
                    y = 0;
                    break;
                case BOTTOM:
                    x = (width - targetWidth) / 2;
                    y = height - targetHeight;
                    break;
                case LEFT:
                    x = 0;
                    y = (height - targetHeight) / 2;
                    break;
                case RIGHT:
                    x = width - targetWidth;
                    y = (height - targetHeight) / 2;
                    break;
                case CENTER:
                default:
                    x = (width - targetWidth) / 2;
                    y = (height - targetHeight) / 2;
                    break;
            }
        } else if (height > targetHeight) {
            switch (cropType) {
                case TOP:
                    x = 0;
                    y = 0;
                    break;
                case BOTTOM:
                    x = 0;
                    y = height - targetHeight;
                    break;
                case LEFT:
                    x = (width - targetWidth) / 2;
                    y = (height - targetHeight) / 2;
                    break;
                case RIGHT:
                    x = (width - targetWidth) / 2;
                    y = (height - targetHeight) / 2;
                    break;
                case CENTER:
                default:
                    x = (width - targetWidth) / 2;
                    y = (height - targetHeight) / 2;
                    break;
            }
        }

        // 执行裁剪或调整大小
        BufferedImage croppedImage;
        if (x >= 0 && y >= 0 && (width >= targetWidth || height >= targetHeight)) {
            // 从指定位置裁剪
            croppedImage = Thumbnails.of(originalImage)
                    .sourceRegion(x, y, targetWidth, targetHeight)
                    .size(targetWidth, targetHeight)
                    .asBufferedImage();
        } else {
            // 直接调整大小
            croppedImage = Thumbnails.of(originalImage)
                    .size(targetWidth, targetHeight)
                    .asBufferedImage();
        }

        // 确保最终图片是目标比例（如果原图比目标比例小，可能需要填充）
        if (Math.abs((double) croppedImage.getWidth() / croppedImage.getHeight() -
                (double) targetRatioW / targetRatioH) > 0.01) {
            // 创建新的目标比例画布
            int finalWidth = croppedImage.getWidth();
            int finalHeight = croppedImage.getHeight();
            if ((double) finalWidth / finalHeight > (double) targetRatioW / targetRatioH) {
                finalHeight = (int) (finalWidth * (double) targetRatioH / targetRatioW);
            } else {
                finalWidth = (int) (finalHeight * (double) targetRatioW / targetRatioH);
            }

            BufferedImage finalImage = new BufferedImage(
                    Math.max(croppedImage.getWidth(), finalWidth),
                    Math.max(croppedImage.getHeight(), finalHeight),
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g = finalImage.createGraphics();
            g.setColor(Color.WHITE); // 使用白色背景填充
            g.fillRect(0, 0, finalImage.getWidth(), finalImage.getHeight());
            int offsetX = (finalImage.getWidth() - croppedImage.getWidth()) / 2;
            int offsetY = (finalImage.getHeight() - croppedImage.getHeight()) / 2;
            g.drawImage(croppedImage, offsetX, offsetY, null);
            g.dispose();
            croppedImage = finalImage;
        }

        // 将裁剪后的图片转回 InputStream
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        String formatName = getFileExtension(file.getOriginalFilename());
        ImageIO.write(croppedImage, formatName, os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());

        // 构建自定义的 MultipartFile
        return new CustomMultipartFile(
                file.getOriginalFilename(),
                file.getContentType(),
                is,
                os.size()
        );
    }

    /**
     * 从文件名获取扩展名
     */
    private static String getFileExtension(String filename) {
        if (filename == null) {
            return "jpg";
        }
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0) {
            return filename.substring(dotIndex + 1).toLowerCase();
        }
        return "jpg";
    }

    /**
     * 重载方法，提供默认裁剪方式（CENTER）
     */
    public static MultipartFile cropImageToRatio(MultipartFile file,
                                                 int targetRatioW,
                                                 int targetRatioH) throws IOException {
        return cropImageToRatio(file, targetRatioW, targetRatioH, CropType.CENTER);
    }

    /**
     * 自定义 MultipartFile 实现
     */
    private static class CustomMultipartFile implements MultipartFile {
        private final String name;
        private final String contentType;
        private final InputStream inputStream;
        private final long size;

        public CustomMultipartFile(String name, String contentType, InputStream inputStream, long size) {
            this.name = name;
            this.contentType = contentType;
            this.inputStream = inputStream;
            this.size = size;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getOriginalFilename() {
            return name;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean isEmpty() {
            return size == 0;
        }

        @Override
        public long getSize() {
            return size;
        }

        @Override
        public byte[] getBytes() throws IOException {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[16384];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            return buffer.toByteArray();
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return inputStream;
        }

        @Override
        public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
            try (InputStream is = getInputStream();
                 java.io.FileOutputStream fos = new java.io.FileOutputStream(dest)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }
        }
    }
}