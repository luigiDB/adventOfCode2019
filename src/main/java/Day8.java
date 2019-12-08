import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Day8 {


    public int[] getLevelWithLessZero(String puzzleInput, int wide, int tall) {
        List<int[]> colorFrequencyByColor = getLevelsColorFrequency(puzzleInput, wide, tall);
        return colorFrequencyByColor.stream().min(Comparator.comparingInt(x -> x[0])).get();
    }

    private List<int[]> getLevelsColorFrequency(String puzzleInput, int wide, int tall) {
        List<int[]> results = new LinkedList<>();

        List<String> levels = parseLevels(puzzleInput, wide, tall);

        for (String level : levels) {
            int[] colorFrequency = new int[3];
            for (int i = 0; i < level.length(); i++) {
                colorFrequency[level.charAt(i) - '0']++;
            }
            results.add(colorFrequency);
        }
        return results;
    }

    private List<String> parseLevels(String puzzleInput, int wide, int tall) {
        List<String> levels = new LinkedList<>();
        for (int i = 0; i < puzzleInput.length(); i += wide * tall) {
            levels.add(puzzleInput.substring(i, i + (wide * tall)));
        }

        for (String level : levels)
            if (level.length() != wide * tall)
                throw new RuntimeException();
        return levels;
    }

    public String decodeImage(String puzzleInput, int wide, int tall) {
        List<String> levels = parseLevels(puzzleInput, wide, tall);
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < wide * tall; i++) {
            for (String level : levels) {
                char c = level.charAt(i);
                if (c != '2') {
                    sb.append(c);
                    break;
                }
            }
        }
        return sb.toString();
    }
}
