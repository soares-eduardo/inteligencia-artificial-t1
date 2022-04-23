import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class App {

    private static List<List<String>> populacao = new ArrayList<List<String>>();
    private static List<List<String>> populacaoIntermediaria = new ArrayList<List<String>>();
    private static int numeroAlunos;
    private static List<Integer> manha = new ArrayList<Integer>();
    private static List<Integer> tarde = new ArrayList<Integer>();
    private static ArrayList<List<String>> duplas = new ArrayList<List<String>>();
    private static ArrayList<List<Integer>> preferenciaManha = new ArrayList<List<Integer>>();
    private static ArrayList<List<Integer>> preferenciaTarde = new ArrayList<List<Integer>>();

    public static void main(String[] args) throws Exception {
        Random rand = new Random();

        lerArquivo();
        init();

        for (int i = 0; i < 400; i++) {
            aptidao();
            List<String> melhor = getBest();
            populacaoIntermediaria.add(melhor);
            System.out.println("melhor:" + melhor);
            crossover();
            populacaoIntermediaria.get(0).remove(populacaoIntermediaria.get(0).size() -1);

            if (rand.nextInt(10) == 0) {
                System.out.println("Mutação");
                mutacao();
            }
            
            duplas = (ArrayList<List<String>>) populacaoIntermediaria;

            populacaoIntermediaria = new ArrayList<List<String>>();
        }
    }

    public static void mutacao() {
        Random rand = new Random();
        int quant = rand.nextInt(3) + 1;

        for (int i = 0; i < quant; i++) {
            int individuo = 0;
            while(individuo == 0) {
                individuo = rand.nextInt(numeroAlunos);
            }

            System.out.println( "individuo" + populacaoIntermediaria.get(individuo));

            int posicao1 = 0;
            int posicao2 = 0;
            String dupla1 = ""; 
            String dupla2 = ""; 

            int alunoManhaNovo = manha.get(rand.nextInt(manha.size()));
            int alunoTardeNovo = tarde.get(rand.nextInt(tarde.size()));
            String alunoDuplaNovo = alunoManhaNovo + "-" + alunoTardeNovo;

            for (int j = 0; j < populacaoIntermediaria.get(individuo).size(); j++) {
                if (Integer.parseInt(populacaoIntermediaria.get(individuo).get(j).split("-")[0]) == alunoManhaNovo ) {
                    dupla1 = populacaoIntermediaria.get(individuo).get(j);
                    posicao1 = j;
                }
                if (Integer.parseInt(populacaoIntermediaria.get(individuo).get(j).split("-")[1]) == alunoTardeNovo) {
                    dupla2 = populacaoIntermediaria.get(individuo).get(j);
                    posicao2 = j;
                }
            }
            
            populacaoIntermediaria.get(individuo).remove(dupla1);
            populacaoIntermediaria.get(individuo).remove(dupla2);

            System.out.println("Cromossomo " + individuo + " sofreu mutação nas cargas de indice " + posicao1 + " e " + posicao2);

            populacaoIntermediaria.get(individuo).add(alunoDuplaNovo);
            populacaoIntermediaria.set(individuo, preencherFilhos(populacaoIntermediaria.get(individuo)));

            System.out.println("resultado mutacao:" + populacaoIntermediaria.get(individuo));
        }
    }

    public static void lerArquivo() throws FileNotFoundException {
        Scanner read = new Scanner(new FileReader("pares40.txt"));
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
        for (int i = 0; i < numeroAlunos * 4; i ++) {
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
    }

    public static List<String> torneio () {
        Random rand = new Random();
        List<String> duplas1 = new ArrayList<String>();
        List<String> duplas2;
        duplas1 = populacao.get(rand.nextInt(populacao.size()));
        duplas2 = populacao.get(rand.nextInt(populacao.size()));

        if (Integer.parseInt(duplas1.get(duplas1.size() - 1)) >= Integer.parseInt(duplas2.get(duplas2.size() - 1))) {
            return duplas1;
        } else {
            return duplas2;
        }
    }

    public static void crossover() {
        List<String> pai = torneio();
        List<String> mae = torneio();
        List<String> filhos = new ArrayList<String>();
        int counter = 0;
        Random rand = new Random();
        while (filhos.size() < numeroAlunos) {
            int randPai = rand.nextInt(numeroAlunos); 
            int randMae = rand.nextInt(numeroAlunos);
            if (isFilhoValido(pai.get(randPai), filhos)) {
                filhos.add(pai.get(randPai));
            }
    
            if (isFilhoValido(mae.get(randMae), filhos)) {
                filhos.add(mae.get(randMae));
            }

            counter++;

            if (counter > numeroAlunos * 4) {
                filhos = preencherFilhos(filhos);
            }
        }

        if (populacaoIntermediaria.size() < numeroAlunos) {
            populacaoIntermediaria.add(filhos);
            crossover();
        }
    }

    public static List<String> preencherFilhos(List<String> filhos) {
        int manhaAluno = 0;
        int tardeAluno = 0;
        while (filhos.size() < numeroAlunos) {
            List<Integer> filhosManha = new ArrayList<Integer>();
            List<Integer> filhosTarde = new ArrayList<Integer>();

            filhos.forEach(x -> {
                filhosManha.add(Integer.parseInt(x.split("-")[0]));
                filhosTarde.add(Integer.parseInt(x.split("-")[1]));
            });

            for (int aluno : manha) {
                if (!filhosManha.contains(aluno)) {
                    manhaAluno = aluno;
                }
                if (!filhosTarde.contains(aluno)) {
                    tardeAluno = aluno;
                }
            }

            String duplaAlunoNovo = manhaAluno + "-" + tardeAluno;
            filhos.add(duplaAlunoNovo);
        }

        return filhos;
    }

    public static boolean isFilhoValido(String filho, List<String> filhos) {
        int primeiroCounter = 0;
        int segundoCounter = 0;

        for (int i = 0; i < filhos.size(); i++) {
            if (filhos.get(i).split("-")[0].equals(filho.split("-")[0])) {
                primeiroCounter++;
            }
            if (filhos.get(i).split("-")[1].equals(filho.split("-")[1])) {
                segundoCounter++;
            }
        }

        if (primeiroCounter > 0 || segundoCounter > 0) {
            return false;
        } else {
            return true;
        }
    }

    public static List<String> getBest() {
        int melhor = 0;
        List<String> melhorDupla = new ArrayList<String>();

        for (int i = 0; i < duplas.size(); i++) {
            List<String> duplaLista = duplas.get(i);
            if (Integer.parseInt(duplaLista.get(duplaLista.size()-1)) > melhor) {
                melhor = Integer.parseInt(duplaLista.get(duplaLista.size()-1));
                melhorDupla = duplaLista;
            }
        }
        return melhorDupla;
    } 

    public static void aptidao() {
        duplas.forEach(dupla -> {
            dupla.add(Integer.toString(calculaAptidao(dupla)));
            populacao.add(dupla);
        });
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


