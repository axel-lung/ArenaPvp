package fr.alsatia.utils;

import org.bukkit.Bukkit;
import org.bukkit.World;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

import static org.bukkit.Bukkit.getLogger;

public class RegenMap {

	private static final int BUFFER_SIZE = 1024;

	public RegenMap() {
		// TODO Auto-generated constructor stub
	}

	public void unloadWorld(World world) {
		if (world != null) {
			Bukkit.getServer().unloadWorld(world, true);
		} else {
			getLogger().severe("Erreur lors du déchargement du monde");
		}
	}

	public void deleteWorld(File path) {
		if (path.exists()) {
			try (Stream<Path> stream = Files.walk(path.toPath())) {
				stream.sorted(Comparator.reverseOrder())
						.map(Path::toFile)
						.forEach(file -> {
							if (!file.delete()) {
								getLogger().severe("Erreur pendant la suppression du fichier : " + file.getAbsolutePath());
							}
						});
			} catch (IOException e) {
				getLogger().severe("Erreur pendant la suppression du monde : " + e.getMessage());
			}
		}
	}

	public void copyWorld(File source, File target) {
		try {
			ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat"));
			if(!ignore.contains(source.getName())) {
				if(source.isDirectory()) {
					if(!target.exists())
						target.mkdirs();
					String[] files = source.list();
					assert files != null;
					for (String file : files) {
						File srcFile = new File(source, file);
						File destFile = new File(target, file);
						copyWorld(srcFile, destFile);
					}
				} else {
					InputStream in = new FileInputStream(source);
					OutputStream out = new FileOutputStream(target);
					byte[] buffer = new byte[BUFFER_SIZE];
					int length;
					while ((length = in.read(buffer)) > 0)
						out.write(buffer, 0, length);
					in.close();
					out.close();
				}
			}
		} catch (IOException e) {
			getLogger().severe("Erreur pendant la copie du monde : ");
			e.printStackTrace();
		}
	}



}
