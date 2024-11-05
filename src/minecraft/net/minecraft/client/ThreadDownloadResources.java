package net.minecraft.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public final class ThreadDownloadResources extends Thread {
	private File resourcesFolder;
	boolean closing = false;

	public ThreadDownloadResources(File file1, Minecraft minecraft2) {
		this.setName("Resource download thread");
		this.setDaemon(true);
		this.resourcesFolder = new File(file1, "resources/");
		if(!this.resourcesFolder.exists() && !this.resourcesFolder.mkdirs()) {
			throw new RuntimeException("The working directory could not be created: " + this.resourcesFolder);
		}
	}

	public final void run() {
        try {
            ArrayList<String> arrayList2 = new ArrayList<String>();
            URL uRL3 = new URL("http://www.minecraft.net/resources/");
            BufferedReader bufferedReader4 = new BufferedReader(new InputStreamReader(uRL3.openStream()));
            String string5;
            while ((string5 = bufferedReader4.readLine()) != null) {
                arrayList2.add(string5);
            }
            bufferedReader4.close();
            for (int i = 0; i < arrayList2.size(); ++i) {
                URL url2 = uRL3;
                String string7 = arrayList2.get(i);
                URL uRL6 = url2;
                Label_0250: {
                    try {
                        String[] split;
                        String string8 = (split = string7.split(","))[0];
                        int integer9 = Integer.parseInt(split[1]);
                        Long.parseLong(split[2]);
                        File file7;
                        if (!(file7 = new File(this.resourcesFolder, string8)).exists() || file7.length() != integer9) {
                            file7.getParentFile().mkdirs();
                            this.downloadResource(new URL(uRL6, string8.replaceAll(" ", "%20")), file7);
                            if (this.closing) {
                                break Label_0250;
                            }
                        }
                        int integer6 = (string5 = string8).indexOf("/");
                        string5.substring(0, integer6);
                        string5.substring(integer6 + 1);
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                if (this.closing) {
                    return;
                }
            }
        }
        catch (IOException ex2) {
            ex2.printStackTrace();
        }
	}

	private void downloadResource(URL uRL1, File file2) throws IOException {
		byte[] b3 = new byte[4096];
		DataInputStream dataInputStream5 = new DataInputStream(uRL1.openStream());
		DataOutputStream dataOutputStream6 = new DataOutputStream(new FileOutputStream(file2));
		boolean z4 = false;

		do {
			int i7;
			if((i7 = dataInputStream5.read(b3)) < 0) {
				dataInputStream5.close();
				dataOutputStream6.close();
				return;
			}

			dataOutputStream6.write(b3, 0, i7);
		} while(!this.closing);

	}
}
