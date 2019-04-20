import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URLConnection;

public class MultiThreadDownload extends Thread {

    private long startIndex;
    private long endIndex;

    MultiThreadDownload(long startIndex, long endIndex) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public void run() {
        RandomAccessFile randomAccessFile = null;
        InputStream inputStream = null;

        try {
            randomAccessFile = new RandomAccessFile(Common.FILE, "rw");
            randomAccessFile.seek(startIndex);
            URLConnection urlConnection = URLConnUtils.getURLConnInstance();
            urlConnection.setRequestProperty("Range", "bytes=" + startIndex + "-" + endIndex);
            inputStream = urlConnection.getInputStream();
            byte[] buffer = new byte[Common.BUFFER_SIZE];

            for (int n; -1 != (n = inputStream.read(buffer)); ) {
                randomAccessFile.write(buffer, 0, n);
                Common.COUNT += n;
            }
        } catch (Exception e) {
            try {
                if (randomAccessFile != null) {
                    System.out.println(Thread.currentThread().getName() + " reload");
                    new MultiThreadDownload(randomAccessFile.getFilePointer(), endIndex).start();
                } else {
                    e.printStackTrace();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
