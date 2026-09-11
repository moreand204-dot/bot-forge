package com.lordescanor.botforge;

import android.content.Context;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class BotGeneratorEngine {
    public static File generate(Context c, BotProject b) throws Exception {
        File work = new File(c.getCacheDir(), "botforge_work_" + System.currentTimeMillis());
        if (work.exists()) delete(work);
        work.mkdirs();
        File template = new File(work, "template");
        template.mkdirs();
        InputStream in = c.getAssets().open("oracle_template.zip");
        ZipUtils.unzip(in, template);
        File root = template;
        File[] kids = root.listFiles();
        if (kids != null && kids.length == 1 && kids[0].isDirectory()) root = kids[0];
        File index = new File(root, "index.js");
        String s = read(index);
        s = s.replaceFirst("phoneNumber:\\s*'[^']*'", "phoneNumber: '" + esc(b.number) + "'");
        StringBuilder owners = new StringBuilder("owners: [\n");
        for (Developer d : b.developers) {
            owners.append("        { name: '").append(esc(d.name)).append("', jid: '")
                    .append(esc(cleanNumber(d.number))).append("@s.whatsapp.net' },\n");
        }
        owners.append("    ]");
        s = s.replaceFirst("(?s)owners:\\s*\\[.*?\\n\\s*\\],", owners + ",");
        s = s.replaceFirst("nameBot:\\s*'[^']*'", "nameBot: '" + esc(b.name) + "'");
        s = s.replaceFirst("idChannel:\\s*'[^']*'", "idChannel: '" + esc(b.channelId == null ? "" : b.channelId) + "'");
        s = s.replaceFirst("channel:\\s*'[^']*'", "channel: '" + esc(b.channelUrl) + "'");
        write(index, s);
        write(new File(root, "BOT_INFO.md"), "# " + b.name + "\n\nArabic name: " + b.nameAr
                + "\nNumber: " + b.number + "\nChannel: " + b.channelUrl + "\nChannel ID: "
                + b.channelId + "\nDevelopers: " + b.developers.size() + "\n");
        File dir = new File(c.getExternalFilesDir(null), "BotForge");
        if (!dir.exists()) dir.mkdirs();
        File out = new File(dir, safe(b.name) + ".zip");
        ZipUtils.zip(root, out);
        delete(work);
        return out;
    }
    private static String cleanNumber(String s) { return s == null ? "" : s.replaceAll("[^0-9]", ""); }
    private static String read(File f) throws Exception { return new String(java.nio.file.Files.readAllBytes(f.toPath()), StandardCharsets.UTF_8); }
    private static void write(File f, String s) throws Exception { java.nio.file.Files.write(f.toPath(), s.getBytes(StandardCharsets.UTF_8)); }
    private static String esc(String s) { if (s == null) return ""; return s.replace("\\", "\\\\").replace("'", "\\'").replace("\n", " ").replace("\r", " "); }
    private static String safe(String s) { String x = s == null ? "bot" : s.replaceAll("[^a-zA-Z0-9._-]+", "_"); return x.length() == 0 ? "bot" : x; }
    private static void delete(File f) { if (f.isDirectory()) { File[] x=f.listFiles(); if(x!=null) for(File q:x) delete(q); } f.delete(); }
}
