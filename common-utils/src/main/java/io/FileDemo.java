package io;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 文件操作的常用例子
 * 1.介绍了jdk8之前过滤一个目录下的文件名和jdk8之后的例子。
 * 2.递归的遍历一个目录下的所有文件
 * 3.使用commons.io拷贝文件、使用NIO的Files拷贝文件、使用缓冲字节流复制文件
 * ---通过实验可以看出拷贝大文件使用commons.io的效率最高
 * 4.使用commons.io每次读取一行文件
 * 5.通过commons.io写文件
 * 总结：apache的commons.io挺好用，在以后的场景中可以举一反三。
 *
 * @author yzzhang
 * @date 2020/9/13 18:57
 */
public class FileDemo {
    private static final String filePath = "C:\\MyProject\\test\\src\\other";
    // 1.18G
    private static final File srcFile = new File("C:\\in\\in\\testile.zip");
    private static final File destFile = new File("C:\\in\\out\\testile.zip");

    /**
     * JAVA8之前 获取扩展名为.java所有文件
     */
    public void filterFile() {
        File file = new File(filePath);
        //获取指定扩展名的文件,由于要对所有文件进行扩展名筛选，因此调用方法需要传递过滤器
        File[] files = file.listFiles(new MyFileFilter());
        //遍历获取到的所有符合条件的文件
        for (File f : files) {
            System.out.println(f);
        }
    }

    //定义类实现文件名称FilenameFilter过滤器
    class MyFileFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return name.endsWith(".java");
        }
    }

    /**
     * JAVA8 获取扩展名为.java所有文件
     *
     * @param filePath 文件路径
     * @throws IOException
     */
    public static void listFiles(String filePath) throws IOException {
        Files.list(Paths.get(filePath))
                .filter(file -> file.toString().endsWith(".java"))
                .forEach(System.out::println);
    }

    /**
     * 递归的遍历一个目录下的所有文件
     *
     * @param file 待遍历的文件路径
     */
    private static void listAllFile(File file) {
        File[] fs = file.listFiles();
        for (File f : fs) {
            if (f.isDirectory()) {   //若是目录，则递归打印该目录下的文件
                listAllFile(f);
            } else if (f.isFile()) {  //若是文件，直接打印
                System.out.println(f);
            }
        }
    }

    /**
     * 使用commons拷贝文件
     */
    public static void copyByCommons(File srcFile, File destFile) throws IOException {
        FileUtils.copyFile(srcFile, destFile);
    }

    /**
     * 使用commons拷贝文件
     */
    public static void copyByNIO(File srcFile, File destFile) throws IOException {
        Files.copy(srcFile.toPath(), destFile.toPath());
    }

    /**
     * 通过缓冲字节流复制文件
     */
    public static void copyByBufferStream(File srcFile, File destFile) {
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(srcFile));
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile));
            // 读写数据
            int len;
            byte[] bytes = new byte[8 * 1024];
            while ((len = bis.read(bytes)) != -1) {
                bos.write(bytes, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过Commons.io读取文件的每一行
     */
    public static void readLineByCommonsIO() throws IOException {
        File file = new File("C:\\MyProject\\test\\src\\other\\CG.java");
        LineIterator it = FileUtils.lineIterator(file, "UTF-8");
        try {
            while (it.hasNext()) {
                String line = it.nextLine();
                System.out.println(line);
            }
        } finally {
            it.close();
        }
    }


    /**
     * 通过Commons.io写文件
     */
    public static void writeByCommonsIO() throws IOException {
        String data = "hello world!\r\n我在学习java";
        File file = new File("C:\\in\\out\\test.txt");
        FileUtils.write(file, data, "utf-8");
    }

    public static void main(String[] args) throws Exception {
        long begin = System.currentTimeMillis();
//        FileDemo fileDemo = new FileDemo();
        //     fileDemo.filterFile();
//      listFiles(filePath);
//      listAllFile(new File("C:\\MyProject\\test"));

        //花费时间:4.188秒
//        copyByCommons(srcFile, destFile);
        //花费时间: 8.901秒
//        copyByNIO(srcFile, destFile);
        // 46.264秒
//        copyByBufferStream(srcFile, destFile);

        writeByCommonsIO();
        long end = System.currentTimeMillis();
        System.out.println("花费时间:" + (end - begin));

    }
}
