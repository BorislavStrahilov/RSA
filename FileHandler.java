
/**
 * ************************************************************************************************
 * Antonio Wayne Shelton - ashelt7@uic.edu 
 * Borislav Strahilov - bstrah2@uic.edu
 *
 * CS342 Project 03 - A java-based implementation of the RSA encryption
 * algorithm
 * 
 * files Version 1.0
 *
 *************************************************************************************************
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.Random;

// File writing code is heavily based on the example at:
// http://www.mkyong.com/java/how-to-write-to-file-in-java-bufferedwriter-example/

public class FileHandler {

	private static File pub_key;
	private static File pri_key;
	private static File blocked;
	private static File unblocked;
	private static File primes;
	private static File crypted_block;

	private static boolean oddLength = false;

	private static FileWriter fw;
	private static FileReader f_reader;
	private static BufferedReader b_reader;

	public static void crypt(File key_file, File blocked_file) {

		// a flag that will tell
		// the program if it should encrypt or
		// decrypt the blocked file
		boolean shouldDecrypt;

		// the n value that will be used
		// by both encrypt and decrypt
		Huint n_num;

		String key_file_name;
		String line;

		// this will be the prime number as a string
		String substring;

		// number of lines the blocked file has
		int num_lines;

		try {

			// these will be used to read the blocked file
			f_reader = new FileReader(blocked_file);
			b_reader = new BufferedReader(f_reader);

			// these will read the key file
			FileReader f_reader_key = new FileReader(key_file);
			BufferedReader b_reader_key = new BufferedReader(f_reader_key);

			key_file_name = key_file.getName();

			// set the flag to false if using public key
			if (key_file_name.compareTo("pubkey.xml") == 0) {
				shouldDecrypt = false;
			} else {
				shouldDecrypt = true;
			}

			// ---------------LINE COUNT IN BLOCKED FILE
			FileReader f_reader_lines = new FileReader(blocked_file);
			BufferedReader b_reader_lines = new BufferedReader(f_reader_lines);
			num_lines = 0;
			while (b_reader_lines.readLine() != null)
				num_lines++;

			f_reader_lines.close();
			b_reader_lines.close();
			// ---------------END LINE COUNT

			// --------ENCRYPT
			if (!shouldDecrypt) {

				// create new file
				crypted_block = new File("encrypted_block.txt");
				crypted_block.createNewFile();

				// used to write to the new file
				fw = new FileWriter(crypted_block);

				// -------------getting E val-------------

				// read and ignore first line of key file
				b_reader_key.readLine();
				line = b_reader_key.readLine();

				// get indeces of the e value
				int num_start_index = line.indexOf('>') + 1;
				int num_end_index = line.lastIndexOf("</");

				Huint e_val = new Huint(line.substring(num_start_index, num_end_index));

				// -----------getting N val-----------------------

				// read next line
				line = b_reader_key.readLine();

				// get indeces of the e value
				num_start_index = line.indexOf('>') + 1;
				num_end_index = line.lastIndexOf("</");

				Huint n_valtmp = new Huint(line.substring(num_start_index, num_end_index));
				Huint n_val = new Huint();
				n_val.add(n_valtmp);

				// run the crypt using the public key values
				// on each line of the blocked file, then
				// store the result into the crypted_block file
				for (int i = 0; i < num_lines; i++) {

					// create a huint from the line read
					Huint line_to_num = new Huint(b_reader.readLine());

					line_to_num.printvalue();

					// the line_to_num huint encrypted
					Huint encrypted = RSA.endecrypt(e_val, n_val, line_to_num);

					boolean should_print = false;

					// print the encrypted number into the new file
					for (int j = encrypted.getSize() - 1; j >= 0; j--) {
						int first_num = encrypted.getVal()[j];
						if (first_num > 0)
							should_print = true;

						if (should_print)
							fw.write("" + encrypted.getVal()[j]);
					}

					// write in a new line
					fw.write(10);

				} // outer for

			} // end encrypt

			// --------DECRYPT
			else {

				// create new file
				crypted_block = new File("decrypted_block.txt");
				crypted_block.createNewFile();

				// used to write to the new file
				fw = new FileWriter(crypted_block);

				// -------------getting D val-------------

				// read and ignore first line of key file
				b_reader_key.readLine();
				line = b_reader_key.readLine();

				// get indeces of the e value
				int num_start_index = line.indexOf('>') + 1;
				int num_end_index = line.lastIndexOf("</");

				Huint d_val = new Huint(line.substring(num_start_index, num_end_index));

				// -----------getting N val-----------------------

				// read next line
				line = b_reader_key.readLine();

				// get indeces of the e value
				num_start_index = line.indexOf('>') + 1;
				num_end_index = line.lastIndexOf("</");

				Huint n_valtmp = new Huint(line.substring(num_start_index, num_end_index));

				Huint n_val = new Huint();
				n_val.add(n_valtmp);


				// run the crypt using the public key values
				// on each line of the blocked file, then
				// store the result into the crypted_block file
				for (int i = 0; i < num_lines; i++) {

					// create a huint from the line read
					Huint line_to_num = new Huint(b_reader.readLine());

					// the line_to_num huint encrypted
					Huint encrypted = RSA.endecrypt(d_val, n_val, line_to_num);
			
					
					int size = encrypted.getlen();
					System.out.println("size: " + size);
					
					boolean should_print = false;
					
					boolean no_more_zero = false;

					// print the encrypted number into the new file
					for (int j = encrypted.getSize() - 1; j >= 0; j--) {
						int first_num = encrypted.getVal()[j];
						if (first_num > 0)
							should_print = true;

						if (should_print){
							if(size == 3 && no_more_zero == false){
								fw.write("0");
								no_more_zero = true;
							}
							
							fw.write("" + encrypted.getVal()[j]);
						}
					}

					// write in a new line
					fw.write(10);

				} // outer for

				System.out.println("Decryption done.");

			} // end decrypt

			// close the readers and writers
			f_reader.close();
			f_reader_key.close();
			b_reader.close();
			b_reader_key.close();
			fw.close();

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public static void generateRandomKeys() {

		// huge ints to hold p and q
		Huint p_num;
		Huint q_num;

		// the lines that p and q will be gotten from
		Random p_line = new Random();
		Random q_line = new Random();

		int p_line_int;
		int q_line_int;

		try {
			// set up all the file handling goodies
			primes = new File("primes.txt");
			f_reader = new FileReader(primes);
			b_reader = new BufferedReader(f_reader);

			// create a new reader because we have to start reading from top
			// of file a second time
			FileReader f_reader2 = new FileReader(primes);
			BufferedReader b_reader2 = new BufferedReader(f_reader2);

			// num of lines in the blocked file
			// since the primes are 9 in length
			// plus the extra invisible chars at the end
			int numLines = (int) (primes.length() / 10);

			p_line_int = p_line.nextInt(numLines);

			q_line_int = q_line.nextInt(numLines);

			// make sure numbers are different
			while (q_line_int == p_line_int)
				q_line_int = q_line.nextInt(numLines);

			// GENERATE THE P
			for (int i = 0; i < p_line_int - 1; i++) {
				b_reader.readLine();
			}

			p_num = new Huint(b_reader.readLine());

			// GENERATE THE Q
			for (int i = 0; i < q_line_int - 1; i++) {
				b_reader2.readLine();
			}

			q_num = new Huint(b_reader2.readLine());

			// create the keys :)
			RSA.keyCreation(q_num, p_num);

			// close the goodies
			b_reader.close();
			b_reader2.close();
			f_reader.close();
			f_reader2.close();

		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public static void createUnblockedFile(File blocked_file) {

		String line;

		try {

			// set up all the file handling goodies
			unblocked = new File("rsa_unblocked_2.txt");
			unblocked.createNewFile();
			f_reader = new FileReader(blocked_file);
			b_reader = new BufferedReader(f_reader);
			fw = new FileWriter(unblocked);

			// every 4 characters will be an extra \n we dont care about
			// except the last line, there it will be a EOF, still dont care

			// num of lines in the blocked file
			double numLines = (blocked_file.length() / 5.0);

			// num of chars in the original text
			// every line will have an unwanted char
			// which will tell us how many chars in the
			// blocked file, then we divide that by two
			double numChars = (blocked_file.length() - numLines) / 2;

			// strings and ints for first and second unblocked chars
			int fuc, suc;
			String fuc_string, suc_string;

			// check if there is an odd number of original chars
			if ((numLines % 1) > 0.0)
				oddLength = true;

			// loop once per line
			for (int i = 0; i < (int) numLines; i++) {
				line = b_reader.readLine();

				// seperate line into substrings
				suc_string = line.substring(0, 2);
				fuc_string = line.substring(2, line.length());

				// create ints from the substrings
				suc = Integer.parseInt(suc_string);
				fuc = Integer.parseInt(fuc_string);

				// convert to the unblocked equivalent
				switch (suc) {

				// null remains at 0
				case 0:
					suc = 0;
					break;

				// vertical tab becomes 11
				case 1:
					suc = 11;
					break;

				// horizontal tab becomes 9
				case 2:
					suc = 9;
					break;

				// new line becomes 10
				case 3:
					suc = 10;
					break;

				// carriage return becomes 13
				case 4:
					suc = 13;
					break;

				// printable chars are just shifted up by 27
				default:
					suc += 27;
				}

				// do the same for fuc
				switch (fuc) {

				// null remains at 0
				case 0:
					fuc = 0;
					break;

				// vertical tab becomes 11
				case 1:
					fuc = 11;
					break;

				// horizontal tab becomes 9
				case 2:
					fuc = 9;
					break;

				// new line becomes 10
				case 3:
					fuc = 10;
					break;

				// carriage return becomes 13
				case 4:
					fuc = 13;
					break;

				// printable chars are just shifted up by 27
				default:
					fuc += 27;
				}

				fw.write(fuc);
				fw.write(suc);

			} // end of for loop

			// if there is an odd # of char, unblock it
			if (oddLength) {
				line = b_reader.readLine();

				// seperate line into substrings
				suc_string = line.substring(0, line.length());

				// create ints from the substrings
				suc = Integer.parseInt(suc_string);

				// convert to the unblocked equivalent
				switch (suc) {

				// null remains at 0
				case 0:
					suc = 0;
					break;

				// vertical tab becomes 11
				case 1:
					suc = 11;
					break;

				// horizontal tab becomes 9
				case 2:
					suc = 9;
					break;

				// new line becomes 10
				case 3:
					suc = 10;
					break;

				// carriage return becomes 13
				case 4:
					suc = 13;
					break;

				// printable chars are just shifted up by 27
				default:
					suc += 27;
				}

				fw.write(suc);
			}

			// close the goodies
			b_reader.close();
			f_reader.close();
			fw.close();

		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public static void createBlockedFile(File rsa_text) {

		// char buffer to hold 2 chars at a time
		char[] block = new char[2];

		// short for first char blocked
		// and second char blocked
		int fcb, scb;

		// the string equivalent of the fcb and scb
		String fcb_string, scb_string;

		try {
			blocked = new File("rsa_blocked_2.txt");
			blocked.createNewFile();
			f_reader = new FileReader(rsa_text);
			b_reader = new BufferedReader(f_reader);
			fw = new FileWriter(blocked);

			// since block size is 2, see how many loops
			// of 2 chars at a time it will take to do the full message
			double loop_times = (double) ((rsa_text.length() - 1) / 2.0d);

			// check if the length is an odd #
			if (((rsa_text.length() - 1) % 2) > 0.0d) {
				oddLength = true;
			}

			// go through the max even length of message divided in half
			for (int i = 0; i < (int) loop_times; i++) {

				// place the two characters read into the buffer
				b_reader.read(block, 0, 2);

				// make sure the unprintable characters
				// correspond to their correct numbers
				// on Troy's table
				switch ((int) block[1]) {

				// null
				case 0:
					fcb = 0; // null stays at 0
					break;

				// vertical tab
				case 11:
					fcb = 1; // vertical tab becomes 1
					break;

				// horizontal tab
				case 9:
					fcb = 2; // horizontal tab becomes 2
					break;

				// new line
				case 10:
					fcb = 3; // new line becomes 3
					break;

				// carriage return
				case 13:
					fcb = 4; // carriage return becomes 4
					break;

				// the rest of the chars
				default:
					fcb = (int) (block[1] - 27); // the value is 27 less
					break;

				}// end switch for fcb

				// switch for scb
				switch ((int) block[0]) {

				// null
				case 0:
					scb = 0; // null stays at 0
					break;

				// vertical tab
				case 11:
					scb = 1; // vertical tab becomes 1
					break;

				// horizontal tab
				case 9:
					scb = 2; // horizontal tab becomes 2
					break;

				// new line
				case 10:
					scb = 3; // new line becomes 3
					break;

				// carriage return
				case 13:
					scb = 4; // carriage return becomes 4
					break;

				// the rest of the chars
				default:
					scb = (int) (block[0] - 27); // the value is 27 less
					break;

				}// end switch for scb

				// add a leading 0 if the number is <10
				if (fcb < 10) {
					fcb_string = "0" + Integer.toString(fcb);
				} else {
					fcb_string = Integer.toString(fcb);
				}

				// do the same for scb
				if (scb < 10) {
					scb_string = "0" + Integer.toString(scb);
				} else {
					scb_string = Integer.toString(scb);
				}

				fw.write(fcb_string + scb_string + "\n");

			}

			// block the last character if message length
			// was an odd number
			if (oddLength) {
				b_reader.read(block, 0, 1);
				scb = (int) (block[0] - 27);
				if (scb < 10) {
					scb_string = "0" + Integer.toString(scb);
				} else {
					scb_string = Integer.toString(scb);
				}
				fw.write(scb_string);
			}

			// close the goodies
			b_reader.close();
			f_reader.close();
			fw.close();

		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public static void createPublicKey(Huint e, Huint n) {

		pub_key = new File("pubkey.xml");

		// used to skip leading 0s
		int first_num = 0;
		boolean should_print;

		try {
			pub_key.createNewFile();
			fw = new FileWriter(pub_key);
			fw.write("<rsakey>\n");

			fw.write("\t<evalue>");

			should_print = false;

			for (int i = e.getSize() - 1; i >= 0; i--) {
				first_num = e.getVal()[i];
				if (first_num > 0)
					should_print = true;

				if (should_print)
					fw.write("" + e.getVal()[i]);
			}
			fw.write("</evalue>\n");

			fw.write("\t<nvalue>");

			should_print = false;

			for (int i = n.getSize() - 1; i >= 0; i--) {
				first_num = n.getVal()[i];
				if (first_num > 0)
					should_print = true;

				if (should_print)
					fw.write("" + n.getVal()[i]);
			}
			fw.write("</nvalue>\n");

			fw.write("</rsakey>");

			fw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public static void createPrivateKey(Huint d, Huint n) {
		pri_key = new File("prikey.xml");

		// used to skip leading 0s
		int first_num = 0;
		boolean should_print;

		try {
			pri_key.createNewFile();
			fw = new FileWriter(pri_key);
			fw.write("<rsakey>\n");

			fw.write("\t<dvalue>");

			should_print = false;

			for (int i = d.getSize() - 1; i >= 0; i--) {
				first_num = d.getVal()[i];
				if (first_num > 0)
					should_print = true;

				if (should_print)
					fw.write("" + d.getVal()[i]);
			}
			fw.write("</dvalue>\n");

			fw.write("\t<nvalue>");

			should_print = false;

			for (int i = n.getSize() - 1; i >= 0; i--) {
				first_num = n.getVal()[i];
				if (first_num > 0)
					should_print = true;

				if (should_print)
					fw.write("" + n.getVal()[i]);
			}
			fw.write("</nvalue>\n");

			fw.write("</rsakey>");

			fw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

}
