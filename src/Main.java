import java.io.File;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;

public class Main {

    public static void main(String[] args) {
        try {
            Common.URL = new URL(args[0]);
            Common.FILE = new File(args[1]);
            File parentFile = Common.FILE.getParentFile();

            if (!parentFile.exists()) {
                if (!parentFile.mkdirs()) {
                    System.out.println("mkdirs fault");
                }
            }

            if (!args[2].equalsIgnoreCase(Common.NULL)) {
                Common.THREAD_COUNT = Integer.parseInt(args[2]);
            }

            if (!args[3].equalsIgnoreCase(Common.NULL)) {
                Common.BUFFER_SIZE = Integer.parseInt(args[3]);
            }

            if (!args[4].equalsIgnoreCase(Common.NULL)) {
                Common.REFERER = args[4];
            }

            if (!args[5].equalsIgnoreCase(Common.NULL)) {
                String[] strings = args[5].split(":");
                Common.PROXY = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(strings[0], Integer.parseInt(strings[1])));
            }
            startDownloadThread();
        } catch (ArrayIndexOutOfBoundsException a) {
            try {
                startDownloadThread();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startDownloadThread() throws Exception {
        URLConnection urlConnection = URLConnUtils.getURLConnInstance();
        long contentLength = urlConnection.getContentLengthLong();
        long block = contentLength / Common.THREAD_COUNT;

        for (int i = 0; i < Common.THREAD_COUNT; i++) {
            long startIndex = i * block;
            long endIndex = (i + 1) * block - 1;

            if (i == Common.THREAD_COUNT - 1) {
                endIndex = contentLength - 1;
            }
            new MultiThreadDownload(startIndex, endIndex).start();
        }

        DecimalFormat decimalFormat = new DecimalFormat("0.00%");
        DecimalFormat decimalFormat2 = new DecimalFormat("0.00MB");
        System.out.println("fileSize:" + decimalFormat2.format((contentLength / 1024) / 1024));
        System.out.println("==============================================");

        while (Thread.activeCount() > 1) {
            Thread.sleep(1000);
            System.out.print(decimalFormat2.format((Common.COUNT / 1024) / 1024) + "-----");
            System.out.println(decimalFormat.format(Common.COUNT / contentLength));
        }

        System.out.println("==============================================");
        System.out.println("downloadComplete");
    }
}
