# crowdmix

This code does not contain the Twitter key and secret for the application. For obvious reasons! 
Place a class named `TwitterSecret` in package `com.pij.crowdmix.config` with this content:


    public class TwitterSecret {

        public static String getTwitterKey() {
            return <Your app's twitter key>;
        }

        public static String getTwitterSecret() {
            return <Your app's twitter secret>;
        }
    }
    
    
Also note that because of the way the Twitter logging API works, Espresso test inherit the login status of the previous 
test thus mey of may not run against a logged-in application, when all Espresso tests are run together. 
So they all check the app is **logged in**.