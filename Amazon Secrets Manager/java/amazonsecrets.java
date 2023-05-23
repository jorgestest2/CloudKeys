/*
 * Cloud Keys 2022 Java Edition- Demo Application
 *
 * Copyright (c) 2023 /n software inc. - All rights reserved. - www.nsoftware.com
 *
 */

import java.io.*;

import cloudkeys.*;


public class amazonsecrets extends ConsoleDemo{

	static String command; // user's command
	static String buffer; // text buffer
	static String[] argument; // arguments following command

	static String secretId;
	static String secretDescription;
	static String data;

	public static void main(String[] args) {

		Amazonsecrets amazonsecrets = new Amazonsecrets();
		try {
			amazonsecrets.addAmazonsecretsEventListener(new DefaultAmazonsecretsEventListener(){

				@Override
				public void error(AmazonsecretsErrorEvent e) {
					System.out.println("Error [" + e.errorCode + "]: " + e.description);
				}

				@Override
				public void secretList(AmazonsecretsSecretListEvent e) {
					System.out.println("   " + e.name);
				}
			});

			amazonsecrets.setAccessKey(prompt("Access key",":",""));
			amazonsecrets.setSecretKey(prompt("Secret key",":",""));

			while (true) {
				DisplayMenu();
				System.out.print("> ");
				command = input();
				argument = command.split("\\s");
				if (argument.length == 0)
					continue;
				switch (argument[0].charAt(0)) {
					case 'l':
						System.out.println("Secrets: ");
						amazonsecrets.listSecrets();
						System.out.println();
						break;
					case 'c':
						secretId = prompt("Secret Id", ":", "");
						secretDescription = prompt("Secret Description", ":", "Test");
						data = prompt("Secret Data", ":", "Test");

						amazonsecrets.setSecretData(data.getBytes());
						amazonsecrets.createSecret(secretId, secretDescription);

						System.out.println("Secret Created Successfully");
						break;
					case 'd':
						secretId = prompt("Secret Id", ":", "");
						amazonsecrets.deleteSecret(secretId,7);
						System.out.println("Secret Deleted Successfully");
						break;
					case 'v':
						secretId = prompt("Secret Id", ":", "");
						String versionId = prompt("Version Id", ":", "");
						String stagingLabel = prompt("Staging Label", ":", "");
						amazonsecrets.getSecret(secretId, versionId, stagingLabel);

						System.out.println("Secret Data: " + new String(amazonsecrets.getSecretData()));
						break;
					case '?':
						DisplayMenu();
						break;
					case 'q':
						return;
					default:
						System.out.println("Command Not Recognized.");
				}
			}

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	private static void DisplayMenu() {
		System.out.println("\nAmazonSecrets Commands:");
		System.out.println("l            List Secrets");
		System.out.println("c            Create a Secret");
		System.out.println("d            Delete a Secret");
		System.out.println("v            View a Secret's Data");
		System.out.println("?            Display Options");
		System.out.println("q            Quit");
	}
}
class ConsoleDemo {
  private static BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

  static String input() {
    try {
      return bf.readLine();
    } catch (IOException ioe) {
      return "";
    }
  }
  static char read() {
    return input().charAt(0);
  }

  static String prompt(String label) {
    return prompt(label, ":");
  }
  static String prompt(String label, String punctuation) {
    System.out.print(label + punctuation + " ");
    return input();
  }

  static String prompt(String label, String punctuation, String defaultVal)
  {
	System.out.print(label + " [" + defaultVal + "] " + punctuation + " ");
	String response = input();
	if(response.equals(""))
		return defaultVal;
	else
		return response;
  }

  static char ask(String label) {
    return ask(label, "?");
  }
  static char ask(String label, String punctuation) {
    return ask(label, punctuation, "(y/n)");
  }
  static char ask(String label, String punctuation, String answers) {
    System.out.print(label + punctuation + " " + answers + " ");
    return Character.toLowerCase(read());
  }

  static void displayError(Exception e) {
    System.out.print("Error");
    if (e instanceof CloudKeysException) {
      System.out.print(" (" + ((CloudKeysException) e).getCode() + ")");
    }
    System.out.println(": " + e.getMessage());
    e.printStackTrace();
  }
}




