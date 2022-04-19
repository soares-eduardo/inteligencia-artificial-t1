package com.company;

import java.util.Arrays;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        int[] carga = {5, 5, 10, 15, 3, 10, 2, 16, 9, 7, 5, 5, 10, 15, 3, 10, 2, 16, 9, 7};

        int[][] populacao = new int[11][21];
        int[][] populacaoIntermediaria = new int[11][21];

        Random random = new Random();

        for (int i = 0; i < populacao.length; i++) {
            for (int j = 0; j < 20; j++) {
                populacao[i][j] = random.nextInt(2);
            }
        }

        for (int g = 0; g < 5; g++) {
            System.out.println("Geracao: " + g);
            int p0, p1;
            int menor = 99999;
            int linhaElitismo = 0;
            for (int i = 0; i < populacao.length; i++) {
                p0 = p1 = 0;
                for (int j = 0; j < 20; j++) {
                    if (populacao[i][j] == 0) {
                        p0 += carga[j];
                    } else {
                        p1 += carga[j];
                    }
                }
                populacao[i][20] = Math.abs(p0 - p1);
                if (populacao[i][20] < menor) {
                    menor = populacao[i][20];
                    linhaElitismo = i;
                }
            }


            for (int i = 0; i < populacao.length; i++) {
                for (int j = 0; j < 20; j++) {
                    System.out.print(populacao[i][j] + " ");
                }
                System.out.println(": " + populacao[i][20]);
            }


            populacaoIntermediaria[0] = Arrays.copyOf(populacao[linhaElitismo], 21);

//            for (int i = 0; i < 20; i++) {
//                System.out.print(populacaoIntermediaria[0][i] + " ");
//            }
//            System.out.println();

            int pai1 = realizarTorneio(populacao, random);
            int pai2 = realizarTorneio(populacao, random);

            fazerGeracao(populacaoIntermediaria, populacao[pai1], populacao[pai2]);

//            System.out.println("População Intermediaria gerada:");

//        for (int i = 0; i < populacaoIntermediaria.length; i++) {
//            for (int j = 0; j < 20; j++) {
//                System.out.print(populacaoIntermediaria[i][j] + " ");
//            }
//            System.out.println();
//        }
            populacao = populacaoIntermediaria;
        }
    }

    private static void fazerGeracao(int[][] populacaoIntermediaria, int[] ints, int[] ints1) {
        for (int i = 1; i < populacaoIntermediaria.length; i += 2) {
            for (int j = 0; j < 10; j++) {
                populacaoIntermediaria[i][j] = ints[j];
                populacaoIntermediaria[i + 1][j] = ints1[j];
            }

            for (int j = 10; j < 20; j++) {
                populacaoIntermediaria[i][j] = ints1[j];
                populacaoIntermediaria[i + 1][j] = ints[j];
            }
        }
    }

    private static int realizarTorneio(int[][] populacao, Random random) {
        int t1, t2;
        t1 = random.nextInt(11);
        t2 = random.nextInt(11);

        if (populacao[t1][20] < populacao[t2][20]) {
            return t1;
        } else {
            return t2;
        }
    }
}
