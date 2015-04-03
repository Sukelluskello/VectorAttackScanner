package com.metova.android.util.text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Utility methods for handling Strings.
 */
public class Strings {

    public static final String NULL = "NULL";

    private static String newLine = null;

    private Strings() {

    }

    /**
     * Creates a hexadecimal String from the given array of bytes.
     * 
     * @param bytes  an array of bytes.
     * @return  hexadecimal String.
     */
    public static String bytesToHexString( byte[] bytes ) {

        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {

            String hexString = Integer.toHexString( 0xFF & bytes[i] );
            stringBuffer.append( ( hexString.length() == 1 ) ? "0" : "" );
            stringBuffer.append( hexString );
        }

        return stringBuffer.toString();
    }

    /**
     * Creates a substring of the given String.
     * 
     * @param input  the original String.
     * @param limit  the maximum length for the substring.
     * @return  the substring (length &lt;= limit).
     */
    public static String limit( String input, int limit ) {

        input = ( input == null ) ? "" : input;
        limit = Math.min( input.length(), limit );
        return input.substring( 0, limit );
    }

    /**
     * The Levenshtein distance (or edit distance) is the minimum number of operations needed 
     * to transform the source String into the target String.  This determines the 
     * amount of difference between the given Strings, which is useful in detecting simple 
     * typos or performing loose word matching.
     * 
     * @param source  the source String.
     * @param target  the target String.
     * @param caseSensitive  if false, case sensitivity will be ignored in the distance calculation.
     * @return  the calculated Levenshtein distance.
     */
    public static int levenshteinDistance( String source, String target, boolean caseSensitive ) {

        try {
            if ( caseSensitive ) {
                String modSource = ( source == null ) ? null : source.toUpperCase();
                String modTarget = ( target == null ) ? null : target.toUpperCase();
                return levenshteinDistance( modSource, modTarget );
            }
            else {
                return levenshteinDistance( source, target );
            }
        }
        catch (Throwable t) {
            throw new RuntimeException( "Error computing edit distance. Source: " + source + " Target: " + target );
        }
    }

    /**
     * Helper method used in calculating the Levenshtein distance of two Strings.
     * 
     * @param source  the source String.
     * @param target  the target String.
     * @return  the calculated Levenshtein distance.
     */
    private static int levenshteinDistance( String source, String target ) {

        if ( source == null && target == null ) {
            return 0;
        }
        if ( source == null ) {
            return target.length();
        }
        if ( target == null ) {
            return source.length();
        }

        int n = source.length();
        int m = target.length();

        if ( n == 0 )
            return m;
        if ( m == 0 )
            return n;

        int[][] d = new int[n + 1][m + 1];

        for (int i = 0; i <= n; d[i][0] = i++)
            ;
        for (int j = 1; j <= m; d[0][j] = j++)
            ;

        for (int i = 1; i <= n; i++) {
            char sc = source.charAt( i - 1 );
            for (int j = 1; j <= m; j++) {
                int v = d[i - 1][j - 1];
                if ( target.charAt( j - 1 ) != sc )
                    v++;
                d[i][j] = Math.min( Math.min( d[i - 1][j] + 1, d[i][j - 1] + 1 ), v );
            }
        }
        return d[n][m];
    }

    /**
     * Gets the NewLine attribute of the Formatter class
     *
     * @return    The NewLine value
     */
    public static String lineSeparator() {

        if ( newLine == null ) {

            newLine = System.getProperty( "line.separator" );
            if ( newLine == null ) {
                newLine = "\n";
            }
        }

        return newLine;
    }

    /**
     * Retrieves a substring of text from the specified input.  For example, the input "abcdefg" 
     * with the delimiter "e" will return the substring "abcd".  If the specified delimiter can 
     * not be found in the input string, a null string is returned.
     * 
     * @param input  the string which should be searched for a given delimiter.
     * @param delimiter  the string denoting the end of the desired substring.
     * @return string after delimiter, otherwise null.
     */
    public static String substringBefore( String input, String delimiter ) {

        if ( isNull( delimiter ) )
            throw new IllegalArgumentException( "Delimiter cannot be null." );
        if ( isNull( input ) ) {
            return null;
        }
        else {
            int splitPosition = input.indexOf( delimiter );
            if ( splitPosition == -1 ) {
                return null;
            }
            else {
                //splitPosition = splitPosition;// - delimiter.length(); 
                return input.substring( 0, splitPosition );
            }
        }
    }

    /**
     * Creates a substring of the given input from 0 to the
     * nth occurrence of the given delimiter.
     * 
     * time - O(n)
     * object handles - 3 
     * 
     * @param input String to search
     * @param delimiter String to search for
     * @param occurrence A 1-indexed count of which occurrence to end before
     * @return input if occurrence < 1, empty string if there aren't enough 
     *  occurrences in input, substring from 0 to occurrence's index otherwise
     */
    public static String substringBefore( String input, String delimiter, int occurrence ) {

        if ( occurrence < 1 ) {
            return input;
        }
        int start = 0;
        char[] inputArray = input.toCharArray();
        char[] delimiterArray = delimiter.toCharArray();
        for (int i = 0; i < occurrence; i++) {
            start = indexOf( inputArray, start, inputArray.length, delimiterArray, 0, delimiterArray.length, 0 );
            if ( start == -1 ) {
                return "";
            }
        }
        return substringFirst( input, start );
    }

    /**
     * Retrieves a substring of text from the specified input.  For example, the input "abcdefg" 
     * with the delimiter "e" will return the substring "fg".  If the specified delimiter can 
     * not be found in the input string, a null string is returned.
     * 
     * @param input  the string which should be searched for a given delimiter.
     * @param delimiter  the string denoting the text appearing before the start of the desired substring.
     * @return string after delimiter, otherwise null.
     */
    public static String substringAfter( String input, String delimiter ) {

        if ( isNull( delimiter ) )
            throw new IllegalArgumentException( "Delimiter cannot be null." );
        if ( isNull( input ) ) {
            return null;
        }
        else {
            int splitPosition = input.indexOf( delimiter );
            if ( splitPosition == -1 ) {
                return null;
            }
            else {
                splitPosition = splitPosition + delimiter.length();
                return input.substring( splitPosition );
            }
        }
    }

    /**
     * Creates a substring of the given String.
     * 
     * @param input  the original String.
     * @param chars  desired length of the substring.
     * @return  the substring.
     */
    public static String substringFirst( String input, int maxChars ) {

        if ( input != null ) {
            if ( input.length() > maxChars ) {
                return input.substring( 0, maxChars );
            }
        }

        return input;
    }

    /**
     * Returns a substring of the given string that has less than or equal to the specified number of characters.
     * 
     * @param text  the text from which we are retrieving a substring.
     * @param maxChars  the maximum number of characters to appear in the resultant substring.
     * @return  Returns a substring of the given string that has less than or equal to the specified number of characters.
     */
    public static String substringLast( String text, int maxChars ) {

        if ( text != null ) {
            if ( text.length() > maxChars ) {
                int startingIndex = text.length() - 8;
                text = text.substring( startingIndex );
            }
        }

        return text;
    }

    /**
     * Determines the index of the first number in the string.
     * 
     * @param text
     * @return The index of the first numerical character in the String, or -1 is no numerical characters exist.
     */
    public static int indexOfNumber( String text ) {

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt( i );
            if ( isNumeric( new String( new char[] { c } ) ) ) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Returns true if a string is entirely numeric, false otherwise.
     * 
     * @param text  the text which may or may not be entirely numeric.
     * @return Returns true if a string is entirely numeric, false otherwise.
     */
    public static boolean isNumeric( String text ) {

        boolean numeric = true;
        try {
            Double.parseDouble( text );
        }
        catch (NumberFormatException e) {
            numeric = false;
        }

        return numeric;
    }

    /**
     * Returns true if a string is entirely alphabetic, false otherwise.
     * 
     * @param text  the text which may or may not be entirely alphabetic.
     * @return Returns true if a string is entirely alphabetic, false otherwise.
     */
    public static boolean isAlphabetic( String text ) {

        return text.matches( "[A-Za-z]*" );
    }

    /**
     * Returns true if a string is entirely alphanumeric, false otherwise.
     * 
     * @param text  the text which may or may not be entirely alphanumeric.
     * @return Returns true if a string is entirely alphanumeric, false otherwise.
     */
    public static boolean isAlphanumeric( String text ) {

        for (int i = 0; i < text.length(); i++) {

            if ( !Character.isLetterOrDigit( text.charAt( i ) ) ) {
                return false;
            }
        }

        return true;
    }

    /**
     * Determines whether a String contains a particular String of characters within it.
     * 
     * @param input  the String that will be searched.
     * @param contains  the substring that may be found within the String being searched.
     * @return  true if the substring was found within the String being searched.
     */
    public static boolean contains( String input, String contains ) {

        if ( !Strings.isNull( input ) ) {
            return input.indexOf( contains ) != -1;
        }
        else {
            return false;
        }
    }

    /**
     * Determines whether a String contains a particular String of characters within it (case insensitively).
     * 
     * @param input  the String that will be searched.
     * @param contains  the substring that may be found within the String being searched.
     * @return  true if the substring was found within the String being searched.
     */
    public static boolean containsIgnoreCase( String input, String contains ) {

        input = input.toLowerCase();
        contains = contains.toLowerCase();

        return contains( input, contains );
    }

    /**
     * Creates an array from a given String using the specified delimiter. 
     * 
     * @param xDelimitedString  the complete String.
     * @param x  the delimiter for the complete String.
     * @return  array of Strings or null if the xDelimitedString is null
     */
    public static String[] getArrayFromXDelimitedString( String xDelimitedString, String x ) {

        List<String> list = getListFromXDelimitedString( xDelimitedString, x );
        if ( list != null ) {

            return list.toArray( new String[list.size()] );
        }
        else {

            return null;
        }
    }

    /**
     * Creates a List from a given String using the specified delimiter.
     * 
     * @param xDelimitedString  the complete String.
     * @param x  the delimiter for the complete String.
     * @return  List of Strings.
     */
    private static List<String> getListFromXDelimitedString( String xDelimitedString, String x ) {

        List<String> list = new ArrayList<String>();
        if ( isNull( xDelimitedString ) ) {
            return null;
        }
        String temp = xDelimitedString;
        while (true) {
            int xIndex = temp.indexOf( x );
            if ( xIndex < 0 ) {
                break;
            }
            list.add( temp.substring( 0, xIndex ).trim() );
            temp = temp.substring( xIndex + 1 );
        }
        temp = trim( temp );
        if ( temp.length() > 0 ) {
            list.add( temp );
        }
        return list;
    }

    /**
     * Determines if the given String is null.
     *
     * @param text  some text 
     * @return true if text == null or ""
     */
    public static boolean isNull( String text ) {

        return ( text == null ) || ( text.length() == 0 );
    }

    /**
     * Determines if the given String is null or consists entirely of whitespace
     * @param text
     * @return
     */
    public static boolean isNullOrWhiteSpace( String text ) {

        return isNull( text ) || text.trim().length() == 0;
    }

    /**
     * Determines if the given String is not null and has non whitespace characters.
     * 
     * @param text  some text
     * @return true if text != null and text contains non whitespace characters 
     */
    public static boolean hasContent( String text ) {

        return !isNull( text ) && !isWhitespace( text );
    }

    /**
     * Determines whether the two given Strings are equal, ignoring case  
     * 
     * @param s1  a String.
     * @param s2  another String.
     * @return  true if the two Strings are equivalent or if both Strings are null
     */
    public static boolean equalsIgnoreCase( String s1, String s2 ) {

        if ( !isNull( s1 ) ) {

            s1 = s1.toLowerCase();
        }
        if ( !isNull( s2 ) ) {

            s2 = s2.toLowerCase();
        }
        return equals( s1, s2, true );
    }

    /**
     * Determines whether the two given Strings are equal.  
     * 
     * @param s1  a String.
     * @param s2  another String.
     * @return  true if the two Strings are equivalent or if both Strings are null
     */
    public static boolean equals( String s1, String s2 ) {

        return equals( s1, s2, true );
    }

    /**
     * 
     * @param s1
     * @param s2
     * @param allowNull This is the value that's returned if both String arguments are null. 
     * @return Returns true if the specified Strings are equivalent, false otherwise.
     */
    public static boolean equals( String s1, String s2, boolean allowNull ) {

        if ( s1 == null && s2 == null ) {

            return allowNull;
        }
        else if ( s1 == null ) {

            return false;
        }
        else {

            return s1.equals( s2 );
        }
    }

    /**
     * Trims surrounding whitespace from the given String.
     * 
     * @param s  the String to be trimmed.
     * @return  the trimmed String.
     */
    public static String trim( String s ) {

        return trim( s, false );
    }

    public static String trim( String s, boolean returnNullIfEmpty ) {

        if ( s == null ) {
            return null;
        }

        String trim = s.trim();
        return returnNullIfEmpty && isNull( trim ) ? null : trim;
    }

    /**
     * Creates a lower-case copy of the String.
     * 
     * @param text  the original String.
     * @return  lower-case String.
     */
    public static String toLowerCase( String text ) {

        if ( text != null ) {

            return text.toLowerCase();
        }

        return null;
    }

    /**
     * Creates an upper-case copy of the String.
     * 
     * @param text  the original String.
     * @return  upper-case String.
     */
    public static String toUpperCase( String text ) {

        if ( text != null ) {

            return text.toUpperCase();
        }

        return null;
    }

    /**
     * Creates a String delimited by the specified delimiter from a given Collection.
     * 
     * @param delimiter  the delimiter for the new String.
     * @param collection  a Collection of Strings.
     * @return  the delimited String.
     */
    public static String getXDelimitedString( String delimiter, Collection<String> collection ) {

        return getXDelimitedString( delimiter, collection, "" );
    }

    /**
     * Creates a String delimited by the specified delimiter from a given Collection.  The 
     * given indentation String will be prepended to each item in the Collection before being 
     * added to the resultant delimited String.
     * 
     * @param delimiter  the delimiter for the new String.
     * @param collection  a Collection of Strings.
     * @param indention  the indentation used in the new String.
     * @return  the delimited and indented String.
     */
    public static String getXDelimitedString( String delimiter, Collection<String> collection, String indention ) {

        if ( collection == null )
            throw new IllegalArgumentException( "collection cannot be null." );
        StringBuffer stringBuffer = new StringBuffer( 20 * collection.size() );
        Iterator<String> iterator = collection.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            i++;
            if ( i != 1 && i <= collection.size() )
                stringBuffer.append( delimiter );
            String s = String.valueOf( iterator.next() );
            stringBuffer.append( indention + s );
        }
        return stringBuffer.toString();
    }

    /**
     * Creates a delimited String from an array of Strings.
     * 
     * @param delimiter  the delimiter for the new String.
     * @param collection  an array of Strings.
     * @return  the delimited String.
     */
    public static String getXDelimitedString( String delimiter, String[] collection ) {

        if ( collection == null )
            throw new IllegalArgumentException( "collection cannot be null." );
        StringBuffer stringBuffer = new StringBuffer( 20 * collection.length );
        for (int i = 0; i < collection.length; i++) {
            if ( i != 0 && i <= collection.length )
                stringBuffer.append( delimiter );
            String s = String.valueOf( collection[i] );
            stringBuffer.append( s );

        }
        return stringBuffer.toString();
    }

    /**
     * Creates a copy of the original String excluding the specified substring.
     * 
     * @param sourceString  the original String.
     * @param stringToRemove  the substring to be removed from the original String.
     * @return  the new String.
     */
    public static String removeAllOccurences( String sourceString, String stringToRemove ) {

        if ( sourceString == null || stringToRemove == null ) {
            return null;
        }
        else {
            if ( sourceString.indexOf( stringToRemove ) != -1 ) {
                return removeAllOccurences( removeOccurence( sourceString, stringToRemove ), stringToRemove );
            }
            return sourceString;
        }
    }

    /**
     * Removes the first occurrence of a substring from the original String.
     * 
     * @param sourceString  the original String.
     * @param stringToRemove  the substring to be removed from the original String.
     * @return  the new String.
     */
    public static String removeOccurence( String sourceString, String stringToRemove ) {

        String returnString = "";
        String firstPart = "";
        String lastPart = "";

        if ( !sourceString.equals( stringToRemove ) ) {

            int startIndex = sourceString.indexOf( stringToRemove );

            // get the part of sourceString before the occurrence if occurrence is found in source
            if ( startIndex >= 0 ) {

                firstPart = sourceString.substring( 0, startIndex );

                // get the part of sourceString after the occurrence if more sourceString exists
                if ( ( startIndex + stringToRemove.length() ) < sourceString.length() ) {

                    lastPart = sourceString.substring( startIndex + stringToRemove.length(), sourceString.length() );
                }

                return firstPart + lastPart;
            }
        }

        return returnString;

    }

    /**
     * Creates a copy of the given String and makes its first letter lower-case.
     *
     * @param s  the original String.
     * @return  String with the first letter in lower-case.
     */
    public static String firstLetterToLowerCase( String s ) {

        if ( isNull( s ) ) {
            return null;
        }

        char[] chars = s.toCharArray();
        chars[0] = Character.toLowerCase( chars[0] );

        return new String( chars );
    }

    /**
     * Creates a copy of the given String and makes its first letter upper-case.
     *
     * @param s  the original String.
     * @return  String with the first letter in upper-case.
     */
    public static String firstLetterToUpperCase( String s ) {

        if ( isNull( s ) ) {
            return null;
        }

        char[] chars = s.toCharArray();
        chars[0] = Character.toUpperCase( chars[0] );

        return new String( chars );
    }

    /**
     * Returns a copy of the given String which will definitely have a 
     * leading slash.
     * 
     * @param value  the original String.
     * @return  String with a leading slash.
     */
    public static String guaranteeStartsWithSlash( String value ) {

        String slash = Character.valueOf( determineSlashChar( value ) ).toString();
        // first guarantee that reference path ends with a slash
        if ( !value.startsWith( slash ) ) {
            value = slash + value;
        }
        return value;
    }

    /**
     * Returns a copy of the given String which will definitely have a 
     * trailing slash.
     * 
     * @param value  the original String.
     * @return  String with a trailing slash.
     */
    public static String guaranteeEndsWithSlash( String value ) {

        String slash = Character.valueOf( determineSlashChar( value ) ).toString();
        // first guarantee that reference path ends with a slash
        if ( !value.endsWith( slash ) ) {
            value += slash;
        }
        return value;
    }

    /**
     * Removes all leading characters matching char leadingCharacterToTrim from String s, and returns the resulting String.
     */
    public static String trimLeadingCharacters( String s, char leadingCharacterToTrim ) {

        for (int index = 0; index < s.length() - 1; index++) {
            char c = s.charAt( index );

            if ( c == leadingCharacterToTrim ) {
                //do nothing, let the loop continue
            }
            else {
                /**
                 * We've found a non matching character, return the String from the current character on.
                 */
                return s.substring( index );
            }
        }

        /**
         * The entire String is made of the leadingCharacterToTrim character
         */
        return "";
    }

    /**
     * Removes all trailing characters matching char trailingCharacterToRemove from String s, and returns the resulting String.
     */
    public static String trimTrailingCharacters( String s, char trailingCharacterToTrim ) {

        for (int index = s.length() - 1; index >= 0; index--) {

            char c = s.charAt( index );

            if ( c == trailingCharacterToTrim ) {
                //do nothing, let the loop continue
            }
            else {
                /**
                 * We've found a non matching character, return the String up to and including this character.
                 */
                return s.substring( 0, index + 1 );

            }

        }

        /**
         * The entire String is made of the trailingCharacterToRemove character
         */
        return "";
    }

    /**
     * Determines whether the slash character is a forward-slash or backward-slash.
     *
     * @param s  the String containing a slash.
     * @return  either a foward-slash or backward-slash.
     */
    public static char determineSlashChar( String s ) {

        char slash;
        int i = s.lastIndexOf( '\\' );
        if ( i <= 0 ) {
            slash = '/';
        }
        else {
            slash = '\\';
        }
        return slash;
    }

    /**
     * Calculates the UTF length of a character array.
     * 
     * @param ch  array of characters.
     * @param start  starting position in the character array.
     * @param length  the maximum length.
     * @return  the calculated length.
     */
    public static int calculateUTFLength( char[] ch, int start, int length ) {

        if ( ch == null || length <= 0 || start < 0 ) {
            return 0;
        }
        //Determine utf length.
        int utflen = 0;
        int c = 0;
        for (int i = 0; i < length; i++) {
            c = ch[i + start];
            if ( ( c >= 0x0001 ) && ( c <= 0x007F ) ) {
                utflen++;
            }
            else if ( c > 0x07FF ) {
                utflen += 3;
            }
            else {
                utflen += 2;
            }
        }
        return utflen;
    }

    /**
     * Splits a String into an array of Strings using the given delimiter.
     *
     * @param input  the original String.
     * @param delim  delimiter (this is converted to a String).
     * @return  array of Strings.
     */
    public static String[] split( String input, char[] delim ) {

        return split( input, delim, true );
    }

    /**
     * Splits a String into an array of Strings using the given delimiter.
     * 
     * @param input  the original String.
     * @param delim  delimiter (this is converted to a String).
     * @param suppressWhitespace
     * @return  array of Strings.
     */
    public static String[] split( String input, char[] delim, boolean suppressWhitespace ) {

        List<String> list = new ArrayList<String>();

        String delimString = String.valueOf( delim );
        if ( isNull( input ) || isNull( delimString ) ) {
            return new String[0];
        }

        int delimLength = delim.length;

        String temp = input;
        while (true) {

            int xIndex = -1;

            outerLoop: for (int i = 0; i < temp.length(); i++) {

                char c = temp.charAt( i );
                for (int j = 0; j < delimLength; j++) {

                    if ( c == delim[j] ) {

                        xIndex = i;
                        break outerLoop;
                    }
                }
            }

            if ( xIndex < 0 ) {

                break;
            }
            else if ( xIndex > 0 ) {

                String part = temp.substring( 0, xIndex );

                if ( suppressWhitespace ) {
                    part = part.trim();
                }

                list.add( part );
            }

            temp = temp.substring( xIndex + 1 );
        }

        if ( suppressWhitespace ) {
            temp = temp.trim();
        }

        if ( temp.length() > 0 ) {
            list.add( temp );
        }

        String[] array = new String[list.size()];
        list.toArray( array );

        return array;
    }

    /**
     * Splits a String into an array of Strings using the given delimiter.
     * 
     * @param input  the original String.
     * @param delim  delimiter (this is converted to a String).
     * @return  array of Strings.
     */
    public static String[] split( String input, char delim ) {

        return split( input, new char[] { delim } );
    }

    /**
     * Determines whether the given String starts with the specified prefix.
     * 
     * @param input  the complete String.
     * @param prefix  the suspected prefix for the String.
     * @return  true if the String begins with the specified prefix.
     */
    public static boolean startsWith( String input, String prefix ) {

        if ( Strings.isNull( input ) ) {

            return false;
        }

        return input.startsWith( prefix );
    }

    /**
     * Determines whether the given String ends with the specified suffix.
     * 
     * @param input  the complete String.
     * @param suffix  the suspected suffix for the String.
     * @return  true if the String ends with the specified suffix.
     */
    public static boolean endsWith( String input, String suffix ) {

        if ( Strings.isNull( input ) ) {

            return false;
        }

        return input.endsWith( suffix );
    }

    public static boolean endsWithIgnoreCase( String string, String suffix ) {

        string = string.toLowerCase();
        suffix = suffix.toLowerCase();

        return string.endsWith( suffix );
    }

    /***
     * Replaces every occurrence of the 'find' string within the 'input' string with the 'replacement' string.
     * 
     * @param input  The string that will be searched through for text to replace.
     * @param find  The text that will be replaced by the 'replacement' string.
     * @param replacement  The string that will replace every occurrence of the 'find' string.
     * @return  String that has had all occurrences of the desired substring replaced.
     */
    public static String replaceAll( String input, String find, String replacement ) {

        StringBuffer stringBuffer = new StringBuffer();

        int indexOf;
        int inputStart = 0;

        while (( indexOf = input.indexOf( find, inputStart ) ) >= 0) {

            if ( indexOf > inputStart ) {

                stringBuffer.append( input.substring( inputStart, indexOf ) );
            }

            stringBuffer.append( replacement );

            inputStart = indexOf + find.length();
        }

        stringBuffer.append( input.substring( inputStart ) );

        return stringBuffer.toString();
    }

    /**
     * Finds the number of occurrences of the specified delimiter in the given String.
     * 
     * @param input  the complete String.
     * @param delim  the delimiter.
     * @return  number of occurrences of the specified delimiter.
     */
    public static int countOccurrences( String input, char delim ) {

        int occurences = split( input, delim ).length - 1;

        if ( input.charAt( 0 ) == delim ) {
            occurences++;
        }

        if ( input.charAt( input.length() - 1 ) == delim ) {
            occurences++;
        }

        return occurences;
    }

    /**
     * @param source
     * @param search
     * @return If source is null, -1 is returned.  Otherwise, the value of source.indexOf(search) is returned.
     */
    public static int indexOf( String source, String search ) {

        if ( source != null ) {

            return source.indexOf( search );
        }

        return -1;
    }

    /**
     * @param source
     * @param sourceOffset
     * @param sourceCount
     * @param target
     * @param targetOffset
     * @param targetCount
     * @param fromIndex
     * @return
     */
    public static int indexOf( char[] source, int sourceOffset, int sourceCount, char[] target, int targetOffset, int targetCount, int fromIndex ) {

        if ( fromIndex >= sourceCount ) {
            return ( targetCount == 0 ? sourceCount : -1 );
        }
        if ( fromIndex < 0 ) {
            fromIndex = 0;
        }
        if ( targetCount == 0 ) {
            return fromIndex;
        }

        char first = target[targetOffset];
        int max = sourceOffset + ( sourceCount - targetCount );

        for (int i = sourceOffset + fromIndex; i <= max; i++) {
            /* Look for first character. */
            if ( source[i] != first ) {
                while (++i <= max && source[i] != first)
                    ;
            }

            /* Found first character, now look at the rest of v2 */
            if ( i <= max ) {
                int j = i + 1;
                int end = j + targetCount - 1;
                for (int k = targetOffset + 1; j < end && source[j] == target[k]; j++, k++)
                    ;

                if ( j == end ) {
                    /* Found whole string. */
                    return i - sourceOffset;
                }
            }
        }
        return -1;
    }

    /**
     * Determines the boolean value the given String.  The String should be one believed to be representative 
     * of a a boolean (e.g., "true" or "false").
     * 
     * @param value  the original String representing a boolean value.
     * @return  The Boolean returned represents the value true if the string argument is not null  and is equal, ignoring case, to the string "true".
     */
    public static boolean booleanValueOf( String value ) {

        if ( Strings.isNull( value ) ) {
            return false;
        }

        return Boolean.valueOf( value );
    }

    /**
     * Ensures that the resultant String is not null.  If the given String is null, this method 
     * shall return a value of "".
     * 
     * @param value  the original String.
     * @return  a non-null String.
     */
    public static String getDisplayValue( String value ) {

        if ( isNull( value ) ) {
            return "";
        }
        else {
            return value;
        }
    }

    /**
     * Determines whether the given String starts with the specified prefix, ignoring case.
     * 
     * @param input  the complete String.
     * @param prefix  the suspected prefix for the String.
     * @return  true if the String begins with the specified prefix.
     */
    public static boolean startsWithIgnoreCase( String string, String prefix ) {

        string = string.toLowerCase();
        prefix = prefix.toLowerCase();

        return string.startsWith( prefix );
    }

    /**
     * @param text
     * @return true if the String only contains whitespace characters.  A whitespace character is determined
     * to be any character whose numeric ASCII value is less than or equal to that of the space character.  This
     * includes all common whitespace characters (including tabs, new lines, and carriage returns).
     */
    public static boolean isWhitespace( String text ) {

        return isNull( text ) || isNull( text.trim() );
    }
}
