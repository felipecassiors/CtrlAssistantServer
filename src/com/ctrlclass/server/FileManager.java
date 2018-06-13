package com.ctrlclass.server;

import java.io.*;
import java.time.Duration;
import java.util.ArrayList;

public class FileManager {

    public static final String TITULO_ARQUIVO_CSV_AUTORIZADOS = "Planilha de Alunos Autorizados";
    public static final String TITULO_ARQUIVO_CSV_MARCACOES = "Planilha de Relatório de Marcações";
    public static final String TITULO_ARQUIVO_CSV_FREQUENCIA = "Planilha de Relatório de Frequência";

    private static final String COMMA_DELIMITER = ";";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String CABECALHO_CSV_MARCACOES = "matricula"+COMMA_DELIMITER+"tipo"+COMMA_DELIMITER+"uid"+COMMA_DELIMITER+"horario";
    private static final String CABECALHO_CSV_FREQUENCIA = "presenca"+COMMA_DELIMITER+"matricula"+COMMA_DELIMITER+"uid"+COMMA_DELIMITER+"entrada"+COMMA_DELIMITER+"saida"+COMMA_DELIMITER+"permanencia"+COMMA_DELIMITER+"fora"+COMMA_DELIMITER+"valido";

    private static final String NOME_ARQUIVO_CSV_MARCACOES = "relatorio_de_marcacoes.csv";
    private static final String NOME_ARQUIVO_CSV_FREQUENCIA = "relatorio_de_frequencia.csv";

    public void criarArquivoCsvMarcacoes (ArrayList<Marcacao> marcacoes) {
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(NOME_ARQUIVO_CSV_MARCACOES);

            fileWriter.append(CABECALHO_CSV_MARCACOES);
            fileWriter.append(NEW_LINE_SEPARATOR);

            for (Marcacao marcacao : marcacoes) {
                if(marcacao.isIn() == null) {
                    fileWriter.append("-");
                    fileWriter.append(COMMA_DELIMITER);
                } else if(marcacao.isIn()) {
                    fileWriter.append("entrada");
                    fileWriter.append(COMMA_DELIMITER);
                } else {
                    fileWriter.append("saida");
                    fileWriter.append(COMMA_DELIMITER);
                }
                if(marcacao.getMatricula() == null) {
                    fileWriter.append("-");
                    fileWriter.append(COMMA_DELIMITER);
                } else {
                    fileWriter.append(marcacao.getMatricula());
                    fileWriter.append(COMMA_DELIMITER);
                }
                fileWriter.append(marcacao.getUid());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(marcacao.getTime().format(Util.TIME_FORMATTER));
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

    public void criarArquivoCsvFrequencia (ArrayList<Aluno> alunos, Duration classTime, Duration toleranceTime) {
        FileWriter fileWriter = null;

        try {
            File recordsDir = new File(System.getProperty("user.home"), ".myApplicationName/records");
            if (! recordsDir.exists()) {
                recordsDir.mkdirs();
            }

            fileWriter = new FileWriter(NOME_ARQUIVO_CSV_FREQUENCIA);

            fileWriter.append("Aula:");
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(Util.formatDuration(classTime));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append("Tolerancia:");
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(Util.formatDuration(toleranceTime));
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.append(NEW_LINE_SEPARATOR);

            fileWriter.append(CABECALHO_CSV_FREQUENCIA);
            fileWriter.append(NEW_LINE_SEPARATOR);

            if (alunos != null) {
                for (Aluno aluno: alunos) {
                    if(aluno.isPresent()) {
                        fileWriter.append("PRESENTE");
                        fileWriter.append(COMMA_DELIMITER);
                    } else {
                        fileWriter.append("AUSENTE");
                        fileWriter.append(COMMA_DELIMITER);
                    }
                    fileWriter.append(aluno.getMatricula());
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(aluno.getUid());
                    fileWriter.append(COMMA_DELIMITER);
                    if(aluno.getInTime() == null) {
                        fileWriter.append("-");
                        fileWriter.append(COMMA_DELIMITER);
                    } else {
                        fileWriter.append(aluno.getInTime().format(Util.TIME_FORMATTER));
                        fileWriter.append(COMMA_DELIMITER);
                    }
                    if(aluno.getOutTime() == null) {
                        fileWriter.append("-");
                        fileWriter.append(COMMA_DELIMITER);
                    } else {
                        fileWriter.append(aluno.getOutTime().format(Util.TIME_FORMATTER));
                        fileWriter.append(COMMA_DELIMITER);
                    }
                    if(aluno.getPermanenceTime().isZero()) {
                        fileWriter.append("-");
                        fileWriter.append(COMMA_DELIMITER);
                    } else {
                        fileWriter.append(Util.formatDuration(aluno.getPermanenceTime()));
                        fileWriter.append(NEW_LINE_SEPARATOR);
                    }
                    if(aluno.getOutsideTime().isZero()) {
                        fileWriter.append("-");
                        fileWriter.append(COMMA_DELIMITER);
                    } else {
                        fileWriter.append(Util.formatDuration(aluno.getOutsideTime()));
                        fileWriter.append(NEW_LINE_SEPARATOR);
                    }
                    if(!aluno.isValidPresence()) {
                        fileWriter.append("NAO");
                        fileWriter.append(COMMA_DELIMITER);
                    } else {
                        fileWriter.append("SIM");
                        fileWriter.append(NEW_LINE_SEPARATOR);
                    }
                }
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
        if (file == null) {
            return null;
        }

        BufferedReader bufferedReader = null;
        ArrayList<Aluno> alunos = null;

        try {

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
