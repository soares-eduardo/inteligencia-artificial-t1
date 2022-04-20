import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.w3c.dom.css.Counter;

public class App {

    private static List<List<String>> populacao = new ArrayList<List<String>>();
    private static int numeroAlunos;
    private static List<Integer> manha = new ArrayList<Integer>();
    private static List<Integer> tarde = new ArrayList<Integer>();
    private static ArrayList<List<String>> duplas = new ArrayList<List<String>>();
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
    }

    public static void init() {
        Random rand = new Random();
        for (int i = 0; i < 10; i ++) {
            int counter = 0;
            List<Integer> alunosManhaDisponiveis = new ArrayList<Integer>(manha);
            List<Integer> alunosTardeDisponiveis = new ArrayList<Integer>(tarde);
            List<String> duplasDefinidas =  new ArrayList<String>();
            while(counter < numeroAlunos) {
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
                    String dupla = alunoManha + "-" + alunoTarde;
                    duplasDefinidas.add(dupla);
                    alunosManhaDisponiveis.set(indexManha, 0);                
                    alunosTardeDisponiveis.set(indexTarde, 0);         
                    counter++;
                }
            }
            duplas.add(duplasDefinidas);
        }
        System.out.println(duplas);

        aptidao();
        // melhor = getBest();
        // System.out.println("Melhor - " + melhor);
    }

    // public static int getBest() {
    //     int melhor = 0;

    //     for (int i = 0; i < duplas.size(); i++) {
    //         if (duplas.get(i).get(2) > melhor) {
    //             melhor = duplas.get(i).get(2);
    //         }
    //     }

    //     return melhor;
    // } 

    public static void aptidao() {
        duplas.forEach(dupla -> {
            System.out.println(calculaAptidao(dupla));
            dupla.add(Integer.toString(calculaAptidao(dupla)));
            populacao.add(dupla);
        });

        System.out.println(populacao);
    }

    public static int calculaAptidao(List<String> cromossomo) {
        int pontuacao = 0;
        for (int i = 0; i < cromossomo.size(); i++) {
            int alunoManha = Integer.parseInt(cromossomo.get(i).split("-")[0]);
            int alunoTarde = Integer.parseInt(cromossomo.get(i).split("-")[1]);

            List<Integer> preferenciaAlunoManha = preferenciaManha.stream().filter(x -> x.get(0) == alunoManha)
                    .collect(Collectors.toList()).get(0);
            List<Integer> preferenciaAlunoTarde = preferenciaTarde.stream().filter(x -> x.get(0) == alunoTarde)
                    .collect(Collectors.toList()).get(0);

            List<Integer> preferenciaAlunoManhaCopia = new ArrayList<Integer>(preferenciaAlunoManha);
            List<Integer> preferenciaAlunoTardeCopia = new ArrayList<Integer>(preferenciaAlunoTarde);

            preferenciaAlunoManhaCopia.remove(0);
            preferenciaAlunoTardeCopia.remove(0);

            pontuacao += (manha.size() - preferenciaAlunoManhaCopia.indexOf(alunoTarde))
                    + (tarde.size() - preferenciaAlunoTardeCopia.indexOf(alunoManha));
        }

        return pontuacao;
    }
}


