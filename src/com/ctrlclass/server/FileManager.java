package com.ctrlclass.server;

import java.io.*;
import java.util.ArrayList;

public class FileManager {

    public static final String TITULO_ARQUIVO_CSV_AUTORIZADOS = "Planilha de Alunos Autorizados";
    public static final String TITULO_ARQUIVO_CSV_MARCACOES = "Planilha de Relatório de Marcações";
    public static final String TITULO_ARQUIVO_CSV_FREQUENCIA = "Planilha de Relatório de Frequência";


    private static final String COMMA_DELIMITER = ";";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String CABECALHO_CSV_MARCACOES = "matricula"+COMMA_DELIMITER+"uid"+COMMA_DELIMITER+"horario";
    private static final String CABECALHO_CSV_FREQUENCIA = "presente"+COMMA_DELIMITER+"matricula"+COMMA_DELIMITER+"uid"+COMMA_DELIMITER+"horario_entrada"+COMMA_DELIMITER+"horario_saida"+COMMA_DELIMITER+"permanencia";

    private static final String NOME_ARQUIVO_CSV_MARCACOES = "relatorio_de_marcacoes.csv";
    private static final String NOME_ARQUIVO_CSV_FREQUENCIA = "relatorio_de_frequencia.csv";

    public void criarArquivoCsvMarcacoes (ArrayList<Marcacao> marcacoes) {
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(NOME_ARQUIVO_CSV_MARCACOES);

            fileWriter.append(CABECALHO_CSV_MARCACOES);
            fileWriter.append(NEW_LINE_SEPARATOR);

            for (Marcacao marcacao : marcacoes) {
                fileWriter.append(marcacao.getMatricula());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(marcacao.getUid());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(marcacao.getHorario().toString());
                fileWriter.append(NEW_LINE_SEPARATOR);
            }

            System.out.println(TITULO_ARQUIVO_CSV_MARCACOES+" foi criado com sucesso.");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void criarArquivoCsvFrequencia (ArrayList<Aluno> alunos) {
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(NOME_ARQUIVO_CSV_FREQUENCIA);

            fileWriter.append(CABECALHO_CSV_FREQUENCIA);
            fileWriter.append(NEW_LINE_SEPARATOR);

            for (Aluno aluno : alunos) {
                fileWriter.append(aluno.isPresent().toString());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(aluno.getMatricula());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(aluno.getUid());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(aluno.getInTime().toString());
                fileWriter.append(NEW_LINE_SEPARATOR);
                fileWriter.append(aluno.getOutTime().toString());
                fileWriter.append(NEW_LINE_SEPARATOR);
                fileWriter.append(aluno.getPermanenceTime().toString());
                fileWriter.append(NEW_LINE_SEPARATOR);
            }

            System.out.println(TITULO_ARQUIVO_CSV_FREQUENCIA+" foi criado com sucesso.");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Aluno> abrirArquivoCsvAutorizados (File file) {
        BufferedReader bufferedReader = null;
        ArrayList<Aluno> alunos = null;

        try {
            if (file == null) {
                throw new FileNotFoundException();
            }

            bufferedReader = new BufferedReader(new FileReader(file));

            alunos = new ArrayList<>();
            String line;

            System.out.println("Alunos autorizados:");

            while ((line = bufferedReader.readLine()) != null) {
                String[] lineSplited = line.split(COMMA_DELIMITER);
                Aluno aluno = new Aluno(lineSplited[0], lineSplited[1]);
                alunos.add(aluno);
                System.out.println("MATRÍCULA: [" + aluno.getMatricula() + "] UID: [" + aluno.getUid() + "]");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao abrir o arquivo "+TITULO_ARQUIVO_CSV_AUTORIZADOS);
        } finally {
            if ( bufferedReader != null ) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return alunos;
        }
    }

}
