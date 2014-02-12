package lib;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class Serializer {

	private static Object recovered = null;

	public Serializer() {

	}

	public static boolean serialize(String filename, Object object) {
		try (OutputStream file = new FileOutputStream(filename);
				OutputStream buffer = new BufferedOutputStream(file);
				ObjectOutput output = new ObjectOutputStream(buffer);) {
			output.writeObject(object);

			return true;
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return false;
	}

	public static boolean deserialize(String filename) {
		try (InputStream file = new FileInputStream(filename);
				InputStream buffer = new BufferedInputStream(file);
				ObjectInput input = new ObjectInputStream(buffer);) {

			// deserialize the List
			recovered = input.readObject();

			return true;
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return false;
	}

	public static Object getRecovered() {
		return recovered;
	}

}
