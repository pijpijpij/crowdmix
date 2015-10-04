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
    
   