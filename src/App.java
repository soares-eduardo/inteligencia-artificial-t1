import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class App {

    private static List<List<Integer>> populacao = new ArrayList<List<Integer>>();
    private static int numeroAlunos;
    private static List<Integer> manha = new ArrayList<Integer>(List.of(1, 2, 3));
    private static List<Integer> tarde = new ArrayList<Integer>(List.of(1, 2, 3));
    private static List<List<Integer>> duplas = new ArrayList<List<Integer>>();
    private static ArrayList<List<Integer>> preferenciaManha = new ArrayList<List<Integer>>();
    private static ArrayList<List<Integer>> preferenciaTarde = new ArrayList<List<Integer>>();
    public static void main(String[] args) throws Exception {
        lerArquivo();
        init();
    }

    public static void lerArquivo() throws FileNotFoundException {
        Scanner read = new Scanner(new FileReader("pares10.txt"));
        int counter = 0;

        while(read.hasNextLine()) {
            if (counter == 0) {
                numeroAlunos = Integer.parseInt(read.nextLine());
            }
            counter++;
            if (counter <= numeroAlunos) {
                List<Integer> tempList = new ArrayList<Integer>(); 

                for (String s : read.nextLine().split(" ")) tempList.add(Integer.valueOf(s));

                preferenciaManha.add(tempList);

                manha.add(counter);
                tarde.add(counter);
            } else {
                List<Integer> tempList = new ArrayList<Integer>();

                for (String s : read.nextLine().split(" "))
                    tempList.add(Integer.valueOf(s));

                preferenciaTarde.add(tempList);
            }
        }

        System.out.println(tarde);
    }

    public static void init() {
        List<Integer> alunosManhaDisponiveis = manha;
        List<Integer> alunosTardeDisponiveis = tarde;
        int melhor = 0;

        Random rand = new Random();
        while(duplas.size() < alunosManhaDisponiveis.size()) {
            int indexManha = rand.nextInt(manha.size());
            int indexTarde = rand.nextInt(tarde.size());
            int alunoManha = alunosManhaDisponiveis.get(indexManha);
            int alunoTarde = alunosTardeDisponiveis.get(indexTarde);
            
            if (alunoManha == 0) {
                indexManha = rand.nextInt(manha.size());
                alunoManha = alunosManhaDisponiveis.get(indexManha);

            } else if (alunoTarde == 0) {
                indexTarde = rand.nextInt(tarde.size());
                alunoTarde = alunosTardeDisponiveis.get(indexTarde);

            } else {
                List<Integer> teste = new ArrayList<Integer>();
                teste.add(alunoManha);
                teste.add(alunoTarde);
                duplas.add(teste);
                alunosManhaDisponiveis.set(indexManha, 0);                
                alunosTardeDisponiveis.set(indexTarde, 0);                
            }
        }

        aptidao();
        melhor = getBest();
        System.out.println(melhor);
    }

    public static int getBest() {
        int melhor = 0;

        for (int i = 0; i < duplas.size(); i++) {
            if (duplas.get(i).get(2) > melhor) {
                melhor = duplas.get(i).get(2);
            }
        }

        return melhor;
    } 

    public static void aptidao() {
        duplas.forEach(dupla -> {
            dupla.add(calculaAptidao(dupla));
            System.out.print("M"+ dupla.get(0));
            System.out.print(",");
            System.out.print("T"+ dupla.get(1));
            System.out.print(" Valor:" + dupla.get(2));
            System.out.println();
        });
    }

    public static int calculaAptidao(List<Integer> dupla) {
        int alunoManha = dupla.get(0);
        int alunoTarde = dupla.get(1);

        List<Integer> preferenciaAlunoManha = preferenciaManha.stream().filter(x -> x.get(0) == alunoManha).collect(Collectors.toList()).get(0);
        List<Integer> preferenciaAlunoTarde = preferenciaTarde.stream().filter(x -> x.get(0) == alunoTarde).collect(Collectors.toList()).get(0);

        List<Integer> preferenciaAlunoManhaCopia = new ArrayList<Integer>(preferenciaAlunoManha);
        List<Integer> preferenciaAlunoTardeCopia = new ArrayList<Integer>(preferenciaAlunoTarde);

        preferenciaAlunoManhaCopia.remove(0);
        preferenciaAlunoTardeCopia.remove(0);        

        int pontuacao = (manha.size() - preferenciaAlunoManhaCopia.indexOf(alunoTarde)) + tarde.size() - preferenciaAlunoTardeCopia.indexOf(alunoManha);        

        return pontuacao;
    }
}


