package app.organicmaps.util;

import android.util.Log;
import android.content.Context;
import android.content.res.AssetManager;
import java.io.*;

public final class MapsBootstrap {
    private static final String ASSET_SUBDIR = "maps"; // inside APK assets
    private static final String DEST_SUBDIR  = "250913";      // inside /files/

    public static void copyMaps(Context ctx) {
        Log.d("MapsBootstrap", "filesRoot=" + ctx.getExternalFilesDir(null));
        File filesRoot = ctx.getExternalFilesDir(null); // …/Android/data/app.organicmaps/files
        if (filesRoot == null) return;
        File destDir = new File(filesRoot, DEST_SUBDIR);
        if (!destDir.exists() && !destDir.mkdirs()) return;

        AssetManager am = ctx.getAssets();
        try {
            String[] files = am.list(ASSET_SUBDIR);
            if (files == null) return;

            byte[] buf = new byte[1 << 16];
            for (String name : files) {
                if (!name.endsWith(".mwm")) continue;
                File outFile = new File(destDir, name);
                Log.d("MapsBootstrap", "Copying " + name + " to " + outFile.getAbsolutePath());
                try (InputStream in = am.open(ASSET_SUBDIR + "/" + name);
                     OutputStream out = new FileOutputStream(outFile)) {
                    int r;
                    while ((r = in.read(buf)) != -1) out.write(buf, 0, r);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MapsBootstrap() {}
}
