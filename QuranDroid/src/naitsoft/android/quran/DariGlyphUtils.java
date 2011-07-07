package naitsoft.android.quran;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Version 1.0
 * This class is responsible for chosing the right glyphs for all persian, dari and arabic words.
 * How to use:
 * 
 * String somePersianWords = "...";
 * String reshapedWords = DariGlyphUtils.reshapeText(somePersianWords);
 * 
 * @author AhmadIV IT-Solution 2011
 *
 */
public class DariGlyphUtils {

	 static HashMap<Integer, String> roots= new HashMap<Integer, String>();
        private static final List<Integer> noChar = new ArrayList<Integer>();
        static{
                noChar.add(8204);
        }
        private static final List<Integer> arabicShats = new ArrayList<Integer>();
        static{
                arabicShats.add(1611);
                arabicShats.add(1612);
                arabicShats.add(1613);
                arabicShats.add(1614);
                arabicShats.add(1615);
                arabicShats.add(1616);
                arabicShats.add(1617);
                arabicShats.add(1618);
        }
        public static int getRootChar(int c){

        	if(c==65152||c==65163||c==65164||c==651522)	return 1569;
        	if(c==65153||c==65153||c==65154||c==65154)	return 1570;
        	if(c==65155||c==65155||c==65156||c==65156)	return 1571;
        	if(c==65157||c==65158||c==65158)return 1572;
        	if(c==65159||c==65160||c==65160)	return 1573;
        	if(c==65161||c==65163||c==65164||c==65162)return 1574;
        	if(c==65165||c==65165||c==65166||c==65166)return 1575;
        	if(c==65167||c==65169||c==65170||c==65168)	return 1576;
        	if(c==65171||c==65171||c==65172||c==65172)		return 1577;
        	if(c==65173||c==65175||c==65176||c==65174)return 1578;
        	if(c==65177||c==65179||c==65180||c==65178)return 1579;
        	if(c==65181||c==65183||c==65184||c==65182)	return 1580;
        	if(c==65185||c==65187||c==65188||c==65186)	return 1581;
        	if(c==65189||c==65191||c==65192||c==65190)return 1582;
        	if(c==65193||c==65193||c==65194||c==65194)return 1583; 
        	if(c==65195||c==65195||c==65196||c==65196)return 1584; 
        	if(c==65197||c==65197||c==65198||c==65198)	return 1585;
        	if(c==65199||c==65199||c==65200||c==65200)	return 1586; 
        	if(c==65201||c==65203||c==65204||c==65202)return 1587;
        	if(c==65205||c==65207||c==65208||c==65206)	return 1588;
        	if(c==65209||c==65211||c==65212||c==65210)return 1589;
        	if(c==65213||c==65215||c==65216||c==65214)	return 1590;
        	if(c==65217||c==65219||c==65218||c==65220)	return 1591;
        	if(c==65221||c==65223||c==65222||c==65222)	return 1592;
        	if(c==65225||c==65227||c==65228||c==65226)return 1593;
        	if(c==65229||c==65231||c==65232||c==65230)return 1594;
        	if(c==65233||c==65235||c==65236||c==65234)	return 1601;
        	if(c==65237||c==65239||c==65240||c==65238)return 1602;
        	if(c==65241||c==65243||c==65244||c==65242)	return 1603;
        	if(c==65245||c==65247||c==65248||c==65246)	return 1604;
        	if(c==65249||c==65251||c==65252||c==65250)return 1605;
        	if(c==65253||c==65255||c==65256||c==65254)return 1606;
        	if(c==65257||c==65259||c==65260||c==65258)return 1607;
        	if(c==65261||c==65261||c==65262||c==65262)return 1608;
        	if(c==65265||c==65267||c==65268||c==65266)	return 1610;
        	if(c==65265||c==65267||c==65268||c==65266)return 1609;
        	if(c==64342||c==64344||c==64345||c==64343)return 1662;
        	if(c==64378||c==64380||c==64381||c==64379)return 1670;
        	if(c==64394||c==64395||c==64395||c==64395)	return 1688;
        	if(c==64398||c==64400||c==64401||c==64399)return 1705;  
        	if(c==64402||c==64404||c==64405||c==64403)return 1711;
        	if(c==65263||c==65267||c==65268||c==65264)    return 1740;
        	return c;
        }
        public static String getRootWord(String w){
        	char[] cs = w.toCharArray();
        	StringBuffer sb = new StringBuffer();
        	for(char c : cs)
        	{
        		sb.append((char)getRootChar(c));
        	}
        	return sb.toString();
        }
        private static final Map<Integer, Glyph> dariGlyphs = new Hashtable<Integer, DariGlyphUtils.Glyph>();  
        static{
                dariGlyphs.put(1569, new Glyph(1569, 65152, 65163, 65164, 65152, 2 ));
                dariGlyphs.put(1570, new Glyph(1570, 65153, 65153, 65154, 65154, 2 ));
                dariGlyphs.put(1571, new Glyph(1571, 65155, 65155, 65156, 65156, 2 ));
                dariGlyphs.put(1572, new Glyph(1572, 65157, 65157, 65158, 65158, 2 ));
                dariGlyphs.put(1573, new Glyph(1573, 65159, 65159, 65160, 65160, 2 ));
                dariGlyphs.put(1574, new Glyph(1574, 65161, 65163, 65164, 65162, 4 ));
                dariGlyphs.put(1575, new Glyph(1575, 65165, 65165, 65166, 65166, 2 ));
                dariGlyphs.put(1576, new Glyph(1576, 65167, 65169, 65170, 65168, 4 ));
                dariGlyphs.put(1577, new Glyph(1577, 65171, 65171, 65172, 65172, 2 ));
                dariGlyphs.put(1578, new Glyph(1578, 65173, 65175, 65176, 65174, 4 ));
                dariGlyphs.put(1579, new Glyph(1579, 65177, 65179, 65180, 65178, 4 ));
                dariGlyphs.put(1580, new Glyph(1580, 65181, 65183, 65184, 65182, 4 ));
                dariGlyphs.put(1581, new Glyph(1581, 65185, 65187, 65188, 65186, 4 ));
                dariGlyphs.put(1582, new Glyph(1582, 65189, 65191, 65192, 65190, 4 ));
                dariGlyphs.put(1583, new Glyph(1583, 65193, 65193, 65194, 65194, 2 ));
                dariGlyphs.put(1584, new Glyph(1584, 65195, 65195, 65196, 65196, 2 ));
                dariGlyphs.put(1585, new Glyph(1585, 65197, 65197, 65198, 65198, 2 ));
                dariGlyphs.put(1586, new Glyph(1586, 65199, 65199, 65200, 65200, 2 ));
                dariGlyphs.put(1587, new Glyph(1587, 65201, 65203, 65204, 65202, 4 ));
                dariGlyphs.put(1588, new Glyph(1588, 65205, 65207, 65208, 65206, 4 ));
                dariGlyphs.put(1589, new Glyph(1589, 65209, 65211, 65212, 65210, 4 ));
                dariGlyphs.put(1590, new Glyph(1590, 65213, 65215, 65216, 65214, 4 ));
                dariGlyphs.put(1591, new Glyph(1591, 65217, 65219, 65218, 65220, 4 ));
                dariGlyphs.put(1592, new Glyph(1592, 65221, 65223, 65222, 65222, 4 ));
                dariGlyphs.put(1593, new Glyph(1593, 65225, 65227, 65228, 65226, 4 ));
                dariGlyphs.put(1594, new Glyph(1594, 65229, 65231, 65232, 65230, 4 ));
                dariGlyphs.put(1600, new Glyph(1600, 1600, 1600, 1600, 1600, 4 ));
                dariGlyphs.put(1601, new Glyph(1601, 65233, 65235, 65236, 65234, 4 ));
                dariGlyphs.put(1602, new Glyph(1602, 65237, 65239, 65240, 65238, 4 ));
                dariGlyphs.put(1603, new Glyph(1603, 65241, 65243, 65244, 65242, 4 ));
                dariGlyphs.put(1604, new Glyph(1604, 65245, 65247, 65248, 65246, 4 ));
                dariGlyphs.put(1605, new Glyph(1605, 65249, 65251, 65252, 65250, 4 ));
                dariGlyphs.put(1606, new Glyph(1606, 65253, 65255, 65256, 65254, 4 ));
                dariGlyphs.put(1607, new Glyph(1607, 65257, 65259, 65260, 65258, 4 ));
                dariGlyphs.put(1608, new Glyph(1608, 65261, 65261, 65262, 65262, 2 ));
                dariGlyphs.put(1609, new Glyph(1609, 65265, 65267, 65268, 65266, 4 ));
                dariGlyphs.put(1610, new Glyph(1610, 65265, 65267, 65268, 65266, 4 ));
                dariGlyphs.put(1662, new Glyph(1662, 64342, 64344, 64345, 64343, 4 ));
                dariGlyphs.put(1670, new Glyph(1670, 64378, 64380, 64381, 64379, 4 ));
                dariGlyphs.put(1688, new Glyph(1688, 64394, 64395, 64395, 64395, 2 ));
                dariGlyphs.put(1705, new Glyph(1705, 64398, 64400, 64401, 64399, 3 ));
                dariGlyphs.put(1711, new Glyph(1711, 64402, 64404, 64405, 64403, 4 ));
                dariGlyphs.put(1740, new Glyph(1740, 65263, 65267, 65268, 65264, 4 ));          
        }
        
        public static String reshapeText(String input){
        		roots.clear();
                        if (input != null) {
                                StringBuffer result = new StringBuffer();
                                String[] sentences = input.split("\n");
                                for (int i = 0; i < sentences.length; i++) {
                                        result.append(reshape(sentences[i]));
                                        result.append("\n");
                                }
                                return result.toString();
                        } else {
                                return null;
                        }
                
        }

        private static Object reshape(String string) {
                String[] words;
                if (string != null) {
                        words = string.split("\\s");
                } else {
                        words = new String[0];
                }

                StringBuffer reshapedText=new StringBuffer("");

                String glyphString;
                for(int i=0;i<words.length;i++){

                		glyphString = getGlyphString(words[i]);
                		roots.put(glyphString.hashCode(), words[i]);
						reshapedText.append(glyphString);
                        reshapedText.append(" ");
                }

                return reshapedText.toString();
        }
        
        
        
        
        
        private static String getGlyphString(String input) {
                char[] inputChars = input.toCharArray();
                List<Glyph> outputGlyphs = new ArrayList<DariGlyphUtils.Glyph>();
                for (int c : inputChars) {
                        Glyph g = dariGlyphs.get(c);
                        if(g == null){
                                outputGlyphs.add(new Glyph(c));
                        }else{
                                outputGlyphs.add(g.clone());
                        }
                }
                reshapeChars(0, outputGlyphs);
                char[] outputChars = new char[outputGlyphs.size()];
                for (int i = 0 ; i < outputGlyphs.size() ; i++) {
                        outputChars[i] = (char) outputGlyphs.get(i).selectedGlyph;
                }
                return new String(outputChars);
        }
        
        
        private static void reshapeChars(int i, List<Glyph> outputGlyphs){
                if(outputGlyphs == null || outputGlyphs.size() == 0){
                        return;
                }else if(i == 0){
                        if(noChar.contains(outputGlyphs.get(i).charCode)){
                                outputGlyphs.remove(i);
                                reshapeChars(i, outputGlyphs);
                                return;
                        }
                        if(outputGlyphs.get(i).isDari){
                                outputGlyphs.get(i).selectedGlyph = outputGlyphs.get(i).mainChar;
                        }
                        reshapeChars(i+1, outputGlyphs);
                        return;
                }else if(i >= outputGlyphs.size()){
                        return;
                }else {
                        Glyph previousGlyph = outputGlyphs.get(i-1);
                        for(int j = i-1 ; j >= 0 && arabicShats.contains(previousGlyph.charCode) ; j--){
                                previousGlyph = outputGlyphs.get(j);
                        }
                        Glyph thisGlyph = outputGlyphs.get(i);
                        for(int j = i ; j < outputGlyphs.size() && arabicShats.contains(thisGlyph.charCode) ; j++){
                                thisGlyph = outputGlyphs.get(j);
                        }
                        Glyph nextGlyph = (i+1)< outputGlyphs.size() ? outputGlyphs.get(i+1) : null;
                        for(int j = i+1 ; nextGlyph != null && j < outputGlyphs.size() && arabicShats.contains(nextGlyph.charCode) ; j++){
                                nextGlyph = outputGlyphs.get(j);
                        }
                        if(noChar.contains(thisGlyph.charCode)){
                                outputGlyphs.remove(i);
                                reshapeChars(i, outputGlyphs);
                                return;
                        }
                        if(thisGlyph.isAlf() && previousGlyph.isLam()){
                                //special case for AlfLam
                                outputGlyphs.set(i-1, previousGlyph.getLamAlfClone(thisGlyph.charCode));
                                outputGlyphs.remove(thisGlyph);
                                reshapeChars(i, outputGlyphs);
                                return;
                        }
                        if(nextGlyph != null && previousGlyph.isLam() && previousGlyph.isStarting() && thisGlyph.isLam() && nextGlyph.isHe()){
                                //special case for Allah
                                
                                outputGlyphs.set(i-1, new Glyph(65010));
                                outputGlyphs.remove(nextGlyph);
                                outputGlyphs.remove(thisGlyph);
                                reshapeChars(i, outputGlyphs);
                                return;
                        }
                        if(!thisGlyph.isDari){
                                reshapeChars(i+1, outputGlyphs);
                                return;
                        }
                        
                        if(!previousGlyph.isDari){
                                thisGlyph.selectedGlyph = thisGlyph.mainChar;
                                reshapeChars(i+1, outputGlyphs);
                                return;
                        }
                        previousGlyph.selectNextGlyph();
                        thisGlyph.selectedGlyph = previousGlyph.isTwoShaped()? thisGlyph.mainChar :thisGlyph.endChar;
                        reshapeChars(i+1, outputGlyphs);
                        return;
                }
        }


        public static class Glyph implements Cloneable{
                private static final int HE = 1607;
                public static int ALF_UPPER_MDD = 0x0622;
                public static int ALF_UPPER_HAMAZA = 0x0623;
                public static int ALF_LOWER_HAMAZA = 0x0625;
                public static int ALF = 0x0627;
                public static int LAM = 0x0644;
                
                int charCode;
                int mainChar;
                int startChar;
                int middleChar;
                int endChar;
                
                int nrShapes;
                
                int selectedGlyph;
                
                boolean isDari = false;

                public Glyph(int charCode) {
                        super();
                        this.charCode = charCode;
                        selectedGlyph = charCode;
                        isDari = false;
                }
                public boolean isHe() {
                        return charCode == HE;
                }
                public boolean isStarting() {
                        return selectedGlyph == startChar || selectedGlyph == this.mainChar;
                }
                public boolean isTwoShaped() {
                        return nrShapes == 2;
                }
                public Glyph(int charCode, int mainChar, int startChar,
                                int middleChar, int endChar, int nrShapes) {
                        super();
                        this.charCode = charCode;
                        this.mainChar = mainChar;
                        this.startChar = startChar;
                        this.middleChar = middleChar;
                        this.endChar = endChar;
                        this.nrShapes = nrShapes;
                        isDari = true;
                }
                
                public  void selectNextGlyph(){
                        if(selectedGlyph == 0){
                                this.selectedGlyph = mainChar;
                                return;
                        }
                        if(selectedGlyph == mainChar){
                                this.selectedGlyph = startChar;
                                return;
                        }
                        if(selectedGlyph == endChar){
                                this.selectedGlyph = middleChar;
                                return;
                        }
                        
                }
                public boolean isLam(){
                        return charCode == LAM;
                }
                public boolean isAlf(){
                        return charCode == ALF ||charCode == ALF_LOWER_HAMAZA ||charCode == ALF_UPPER_HAMAZA ||charCode == ALF_UPPER_MDD;   
                }
                public String toString(){
                        return ""+(char)selectedGlyph;
                }
                public Glyph getLamAlfClone(int charCode){
                        boolean isStarting = isStarting();
                        if(charCode == ALF_UPPER_MDD){
                                int c = isStarting ? 65269 : 65270;
                                return new Glyph(c);
                        }else if(charCode == ALF_LOWER_HAMAZA){
                                int c = isStarting ? 65273 : 65274;
                                return new Glyph(c);
                        }else if(charCode == ALF_UPPER_HAMAZA){
                                int c = isStarting ? 65271 : 65272;
                                return new Glyph(c);
                        }else if(charCode == ALF){
                                int c = isStarting ? 65275 : 65276;
                                return new Glyph(c);
                        }
                        return this;
                }
                
                public Glyph clone(){
                        return new Glyph(this.charCode, mainChar, startChar, middleChar, endChar, nrShapes);
                }
        }


		public static HashMap<Integer, String> getRoots() {
			return roots;
		}
		public static void setRoots(HashMap<Integer, String> roots) {
			DariGlyphUtils.roots = roots;
		}

}
