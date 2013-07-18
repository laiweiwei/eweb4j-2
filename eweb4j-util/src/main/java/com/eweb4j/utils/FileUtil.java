package com.eweb4j.utils;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * 文件操作工具类
 * 
 * @author vivi
 * @email l.weiwei@163.com
 * 
 */
public class FileUtil {

	/**
	 * 给文件追加内容 
	 * @date 2013-6-29 上午11:12:57
	 * @param f
	 * @param content
	 * @throws Exception
	 */
	public static void appendFile(File f, String content) throws Exception {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(f, true));
			writer.write(content);
		} catch (Exception e) {
			throw e;
		} finally {
			if (writer != null) {
				try {
					writer.flush();
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 给文件重新写入内容
	 * @date 2013-6-29 上午11:13:08
	 * @param f
	 * @param content
	 * @throws Exception
	 */
	public static void writeFile(File f, String content) throws Exception {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(f), "utf-8"));
			writer.write(content);
		} catch (Exception e) {
			throw e;
		} finally {
			if (writer != null) {
				try {
					writer.flush();
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 读取文件内容
	 * @date 2013-6-29 上午11:13:49
	 * @param f
	 * @return
	 */
	public static String readFile(File f) {
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(f), "utf-8"));
			String line = null;
			while ((line = reader.readLine()) != null)
				sb.append(line);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();
	}
	
	/**
	 * 读取文件每行的内容
	 * @date 2013-6-29 上午11:14:01
	 * @param f
	 * @return
	 */
	public static List<String> readLine(File f) {
		List<String> result = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "utf-8"));
			String line = null;
			while ((line = reader.readLine()) != null)
				result.add(line);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}

	/**
	 * 获取文件后缀名 
	 * @date 2013-6-29 上午11:14:13
	 * @param f
	 * @return
	 */
	public static String getExt(File f) {
		return f.getName().substring(f.getName().lastIndexOf(".") + 1);
	}

	/**
	 * 删除文件夹 
	 * @date 2013-6-29 上午11:14:25
	 * @param folder
	 * @return
	 */
	public static boolean deleteFolder(File folder) {
		return deleteFolderContents(folder) && folder.delete();
	}

	/**
	 * 删除文件夹里的内容
	 * @param folder
	 * @return
	 */
	public static boolean deleteFolderContents(File folder) {
		File[] files = folder.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					if (!file.delete()) {
						return false;
					}
				} else {
					if (!deleteFolder(file)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * 获取图片 
	 * @date 2013-6-29 上午11:15:05
	 * @param imagePath 图片地址
	 * @param isRemote 是否是远程的
	 * @return
	 * @throws Exception
	 */
	public static BufferedImage getBufferedImage(String imagePath, boolean isRemote) throws Exception {
		return getBufferedImage(imagePath, isRemote, 0, 1*1000);
	}
	
	/**
	 * 获取图片 
	 * @date 2013-6-29 上午11:15:20
	 * @param imagePath
	 * @return
	 * @throws Exception
	 */
	public static BufferedImage getBufferedImage(String imagePath) throws Exception {
		return getBufferedImage(imagePath, false, 0, 1*1000);
	}
	
	/**
	 * 获取图片
	 * @date 2013-6-29 上午11:15:53
	 * @param imagePath
	 * @param retryTimes
	 * @param sleep
	 * @return
	 * @throws Exception
	 */
	public static BufferedImage getBufferedImage(String imagePath, int retryTimes, long sleep) throws Exception {
		return getBufferedImage(imagePath, false, retryTimes, sleep);
	}
	/**
	 * 
	 * @param imagePath
	 *            给定的图片Path
	 * @param retryTimes
	 *            如果发生异常重试次数
	 * @param sleep
	 * @return
	 */
	public static BufferedImage getBufferedImage(String imagePath, boolean isRemote, int retryTimes, long sleep) throws Exception {
		if (imagePath == null || imagePath.trim().length() == 0)
			throw new Exception("image url can not be empty");

		int count = 0;
		while (true) {
			try {
				if (isRemote)
					return getRemote(imagePath);
				
				try {
					return getLocal(imagePath);
				} catch (Throwable e) {
					return getRemote(imagePath);
				}
			} catch (Throwable e) {
				if (count >= retryTimes) {
					throw new Exception(e);
				}
				Thread.sleep(sleep);
			}
			count++;
		}
	}

	/**
	 * 获取本地图片
	 * @date 2013-6-29 上午11:16:03
	 * @param imagePath 图片路径
	 * @return
	 * @throws IOException
	 */
	private static BufferedImage getLocal(String imagePath) throws IOException {
		try {
			return toBufferedImage(Toolkit.getDefaultToolkit().getImage(imagePath));
		} catch (Throwable e){
			return ImageIO.read(new File(imagePath));
		}
	}

	/**
	 * 获取远程图片 
	 * @date 2013-6-29 上午11:16:15
	 * @param imagePath
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private static BufferedImage getRemote(String imagePath)
			throws MalformedURLException, IOException {
		URL url = new URL(imagePath.replace(" ","%20"));
		try {
			return toBufferedImage(Toolkit.getDefaultToolkit().getImage(url));
		}catch (Throwable e1){
			return ImageIO.read(url);
		}
	}

	/**
	 * Image转换成BufferedImage 
	 * @date 2013-6-29 上午11:16:27
	 * @param image
	 * @return
	 */
	public static BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}

		// This code ensures that all the pixels in the image are loaded
		image = new ImageIcon(image).getImage();

		// Determine if the image has transparent pixels; for this method's
		// implementation, see e661 Determining If an Image Has Transparent
		// Pixels
		// boolean hasAlpha = hasAlpha(image);

		// Create a buffered image with a format that's compatible with the
		// screen
		BufferedImage bimage = null;
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		try {
			// Determine the type of transparency of the new buffered image
			int transparency = Transparency.OPAQUE;
			/*
			 * if (hasAlpha) { transparency = Transparency.BITMASK; }
			 */

			// Create the buffered image
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
		} catch (HeadlessException e) {
			// The system does not have a screen
		}

		if (bimage == null) {
			// Create a buffered image using the default color model
			int type = BufferedImage.TYPE_INT_RGB;
			// int type = BufferedImage.TYPE_3BYTE_BGR;//by wang
			/*
			 * if (hasAlpha) { type = BufferedImage.TYPE_INT_ARGB; }
			 */
			bimage = new BufferedImage(image.getWidth(null),
					image.getHeight(null), type);
		}

		// Copy image to buffered image
		Graphics g = bimage.createGraphics();

		// Paint the image onto the buffered image
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return bimage;
	}

	/**
	 * 检查给定文件路径是否存在
	 * @date 2013-6-29 上午11:16:45
	 * @param filePath
	 * @return
	 */
	public static boolean exists(String filePath) {
		File dir = new File(CommonUtil.uriDecoding(filePath));
		return dir.exists();
	}

	/**
	 * 返回某目录下所有文件对象
	 * 
	 * @param str
	 * @return
	 */
	public static File[] getFiles(String str) {
		File dir = new File(CommonUtil.uriDecoding(str));
		File[] result = null;
		if (dir.isDirectory()) {
			result = dir.listFiles();
		}

		return result;
	}

	/**
	 * 返回某个类所在包最顶层文件夹
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getTopClassPath(Class<?> clazz) {
		String path = CommonUtil.uriDecoding(clazz.getResource("/").getPath());

		return path;
	}

	public static void main(String[] args) throws Exception {
		String c = FileUtil.readFile(new File("d:/seasky.lrc"));
		System.out.println(c);
	}

	/**
	 * get the jars path
	 * @return
	 */
	public static String getLib() {
		return CommonUtil.uriDecoding(FileUtil.getParent(
				FileUtil.getTopClassPath(FileUtil.class), 1)
				+ "lib");
	}

	/**
	 * 获取文件夹下的所有文件路径
	 * @date 2013-6-29 上午11:17:09
	 * @param parent
	 * @return
	 */
	public static String[] getChildrenPath(File parent) {
		File[] files = parent.listFiles();
		String[] result = new String[files.length];
		for (int i = 0; i < files.length; i++)
			result[i] = CommonUtil.uriDecoding(files[i].getAbsolutePath());

		return result;
	}

	/**
	 * 获取已经加载的jar包列表
	 * @date 2013-6-29 上午11:17:32
	 * @return
	 */
	public static Collection<String> getJars() {
		Collection<String> jars = new HashSet<String>();
		Enumeration<URL> urls;
		try {
			urls = FileUtil.class.getClassLoader().getResources(
					"META-INF/MANIFEST.MF");

			while (urls.hasMoreElements()) {
				URL url = (URL) urls.nextElement();
				String path = url.getFile().replace("file:/", "")
						.replace("!/META-INF/MANIFEST.MF", "");
				jars.add(CommonUtil.uriDecoding(path));
			}
			File jarDir = new File(getLib());
			if (jarDir.isDirectory() && jarDir.exists()) {
				for (File jar : jarDir.listFiles()) {
					jars.add(CommonUtil.uriDecoding(jar.getAbsolutePath()));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return jars;
	}

	public static String getClassPath(String folderName) {
		return getParent(getTopClassPath(FileUtil.class), 1) + folderName;
	}

	/**
	 * 获得类所在文件路径
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getCurrPath(Class<?> clazz) {
		return CommonUtil.uriDecoding(clazz.getResource("/").getPath()
				+ clazz.getName().replace(".", File.separator));
	}

	/**
	 * 创建一个文件夹
	 * 
	 * @param path
	 * @return
	 */
	public static boolean createDir(String path) {
		boolean flag = false;
		File file = new File(CommonUtil.uriDecoding(path));
		if (!file.exists()) {
			if (!file.isDirectory()) {
				flag = file.mkdir();
			}
		}
		return flag;
	}

	/**
	 * 创建一个文件
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static boolean createFile(String path) throws IOException {
		return createFile(path, false);
	}

	/**
	 * 
	 * @param file
	 * @param isDelete
	 * @return
	 * @throws IOException
	 */
	public static boolean createFile(File file, boolean isDelete)
			throws IOException {
		boolean flag = true;
		if (file.exists()) {
			if (isDelete) {
				file.delete();
				file.createNewFile();
			} else {
				flag = false;
			}
		} else {
			file.createNewFile();
		}

		return flag;
	}

	/**
	 * 
	 * @param path
	 * @param isDelete
	 * @return
	 * @throws IOException
	 */
	public static boolean createFile(String path, boolean isDelete)
			throws IOException {
		File file = new File(CommonUtil.uriDecoding(path));

		return createFile(file, isDelete);
	}

	/**
	 * 将oldFile移动到指定目录
	 * 
	 * @param oldFile
	 * @param newDir
	 * @return
	 */
	public static boolean moveFileTo(File oldFile, String newDir) {
		StringBuilder sb = new StringBuilder(newDir);
		sb.append(File.separator).append(oldFile.getName());
		File toDir = new File(CommonUtil.uriDecoding(sb.toString()));
		boolean flag = false;
		if (!toDir.exists()) {
			flag = oldFile.renameTo(toDir);
		}
		return flag;
	}

	/**
	 * 返回当前文件的上层文件夹路径（第几层由参数floor决定）
	 * 
	 * @param f
	 * @param floor
	 * @return
	 */
	public static String getParent(File f, int floor) {
		String result = "";
		if (f != null && f.exists()) {
			for (int i = 0; i < floor; ++i) {
				f = f.getParentFile();
			}

			if (f != null && f.exists()) {
				result = f.getPath();
			}
		}

		return CommonUtil.uriDecoding(result) + File.separator;
	}

	public static String getParent(String path, int floor) {
		return getParent(new File(path), floor);
	}

	/**
	 * 删除文件
	 * 
	 * @param file
	 * @return
	 */
	public static boolean deleteFile(File file) {
		boolean flag = false;
		if (file != null && file.exists()) {
			if (file.isDirectory()) {
				for (File f : file.listFiles()) {
					deleteFile(f);
				}
			}
			flag = file.delete();
		}

		return flag;
	}

	/**
	 * 检查文件名是否合法
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean isValidFileName(String fileName) {
		if (fileName == null || fileName.length() > 255)
			return false;
		else {
			return fileName
					.matches("[^\\s\\\\/:\\*\\?\\\"<>\\|](\\x20|[^\\s\\\\/:\\*\\?\\\"<>\\|])*[^\\s\\\\/:\\*\\?\\\"<>\\|\\.]$");
		}
	}

	/**
	 * 复制文件
	 * 
	 * @param src
	 * @param dst
	 */
	public static void copy(File src, File dst) {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(src);
			out = new FileOutputStream(dst);

			// Transfer bytes from in to out
			byte[] buf = new byte[1024];
			int len = -1;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (out != null) {
						out.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return;
	}

}
