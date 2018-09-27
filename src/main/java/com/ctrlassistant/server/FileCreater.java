package com.ctrlassistant.server;

import com.ctrlassistant.server.model.Clazz;
import com.ctrlassistant.server.model.Student;

import java.io.FileWriter;
import java.util.Map;

public class FileCreater {

    public static final String TITULO_ARQUIVO_CSV_AUTORIZADOS = "Planilha de Alunos Autorizados";
    public static final String TITULO_ARQUIVO_CSV_MARCACOES = "Planilha de Relatório de Marcações";
    public static final String TITULO_ARQUIVO_CSV_FREQUENCIA = "Planilha de Relatório de Frequência";

    private static final String COMMA_DELIMITER = ";";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String CABECALHO_CSV_MARCACOES = "matricula" + COMMA_DELIMITER + "tipo" + COMMA_DELIMITER + "tag" + COMMA_DELIMITER + "horario";
    private static final String CABECALHO_CSV_FREQUENCIA = "presenca" + COMMA_DELIMITER + "nome" + COMMA_DELIMITER + "tag" + COMMA_DELIMITER + "entrada" + COMMA_DELIMITER + "saida" + COMMA_DELIMITER + "permanencia" + COMMA_DELIMITER + "fora" + COMMA_DELIMITER + "valido";

    private static final String NOME_ARQUIVO_CSV_MARCACOES = "relatorio_de_marcacoes.csv";
    private static final String NOME_ARQUIVO_CSV_FREQUENCIA = "relatorio_de_frequencia.csv";

    Clazz clazz;

    public FileCreater(Clazz clazz) {
        this.clazz = clazz;
    }

    /*
    public void criarArquivoCsvMarcacoes (ArrayList<Checking> marcacoes) {
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(NOME_ARQUIVO_CSV_MARCACOES);

            fileWriter.append(CABECALHO_CSV_MARCACOES);
            fileWriter.append(NEW_LINE_SEPARATOR);

            for (Checking check : marcacoes) {
                if(check.isIn() == null) {
                    fileWriter.append("-");
                    fileWriter.append(COMMA_DELIMITER);
                } else if(check.isIn()) {
                    fileWriter.append("entrada");
                    fileWriter.append(COMMA_DELIMITER);
                } else {
                    fileWriter.append("saida");
                    fileWriter.append(COMMA_DELIMITER);
                }
                if(check.getMatricula() == null) {
                    fileWriter.append("-");
                    fileWriter.append(COMMA_DELIMITER);
                } else {
                    fileWriter.append(check.getMatricula());
                    fileWriter.append(COMMA_DELIMITER);
                }
                fileWriter.append(check.getTag());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(check.getDateTime().format(Util.TIME_FORMATTER));
                fileWriter.append(NEW_LINE_SEPARATOR);
            }

            System.out.println(TITULO_ARQUIVO_CSV_MARCACOES+" criado com sucesso");

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
    */


    public void criarArquivoCsvFrequencia() {
        FileWriter fileWriter = null;
        try {

            /*
            File recordsDir = new File(System.getProperty("user.home"), ".myApplicationName/records");
            if (! recordsDir.exists()) {
                recordsDir.mkdirs();
            }
            */

            fileWriter = new FileWriter(NOME_ARQUIVO_CSV_FREQUENCIA);

            fileWriter.append("Aula:");
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(clazz.getDiscipline().getName());
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append("Professor:");
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(clazz.getDiscipline().getTeacher().getName());
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.append("Inicio:");
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(clazz.getStartDateTime().format(Util.TIME_FORMATTER));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append("Termino:");
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(clazz.getFinishDateTime().format(Util.TIME_FORMATTER));
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.append("Duracao:");
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(Util.formatDuration(clazz.getClassTime()));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append("Tolerancia:");
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(Util.formatDuration(clazz.getToleranceTime()));
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.append(NEW_LINE_SEPARATOR);

            fileWriter.append(CABECALHO_CSV_FREQUENCIA);
            fileWriter.append(NEW_LINE_SEPARATOR);

            for (Map.Entry<Integer, Student> entry : clazz.getStudents().entrySet()) {
                Student student = entry.getValue();
                if (student.isPresent()) {
                    fileWriter.append("PRESENTE");
                    fileWriter.append(COMMA_DELIMITER);
                } else {
                    fileWriter.append("AUSENTE");
                    fileWriter.append(COMMA_DELIMITER);
                }
                fileWriter.append(student.getName());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(student.getTag());
                fileWriter.append(COMMA_DELIMITER);
                if (student.getInTime() == null) {
                    fileWriter.append("-");
                    fileWriter.append(COMMA_DELIMITER);
                } else {
                    fileWriter.append(student.getInTime().format(Util.TIME_FORMATTER));
                    fileWriter.append(COMMA_DELIMITER);
                }
                if (student.getOutTime() == null) {
                    fileWriter.append("-");
                    fileWriter.append(COMMA_DELIMITER);
                } else {
                    fileWriter.append(student.getOutTime().format(Util.TIME_FORMATTER));
                    fileWriter.append(COMMA_DELIMITER);
                }
                if (student.getPermanenceTime().isZero()) {
                    fileWriter.append("-");
                    fileWriter.append(COMMA_DELIMITER);
                } else {
                    fileWriter.append(Util.formatDuration(student.getPermanenceTime()));
                    fileWriter.append(COMMA_DELIMITER);
                }
                if (student.getOutsideTime().isZero()) {
                    fileWriter.append("-");
                    fileWriter.append(COMMA_DELIMITER);
                } else {
                    fileWriter.append(Util.formatDuration(student.getOutsideTime()));
                    fileWriter.append(COMMA_DELIMITER);
                }
                if (!student.isValidPresence()) {
                    fileWriter.append("NAO");
                    fileWriter.append(NEW_LINE_SEPARATOR);
                } else {
                    fileWriter.append("SIM");
                    fileWriter.append(NEW_LINE_SEPARATOR);
                }
            }
            fileWriter.flush();
            fileWriter.close();
            System.out.println(TITULO_ARQUIVO_CSV_FREQUENCIA + " criado com sucesso");

            String messageFrequence = "";

            messageFrequence += ("Olá professor "+clazz.getDiscipline().getTeacher().getName()+"!\n\n" +
                    "O relatório de frequência da aula "+clazz.getDiscipline().getName()+", ministrada em "+
                    clazz.getStartDateTime().format(Util.DATE_FORMATTER)+" está em anexo.\n\n" +
                    "Informações da aula\n\n" +
                    "Iniciou às " + clazz.getStartDateTime().format(Util.TIME_FORMATTER) + "\n" +
                    "Terminou às " + clazz.getFinishDateTime().format(Util.TIME_FORMATTER) + "\n" +
                    "Com duração de " + Util.formatDuration(clazz.getClassTime()) + "\n" +
                    "E tolerância de " + Util.formatDuration(clazz.getToleranceTime()) + "\n" +
                    "\nNós somos Ctrl Assistant.");

            messageFrequence += ("\n\n");

/*            messageFrequence += ("Nome\t\tMatrícula\t\tEntrada\t\tSaída\t\tDentro\t\tFora\t\tSituação\t\tVálida\n");
            for (Map.Entry<Integer, Student> entry : clazz.getStudents().entrySet()) {
                Student student = entry.getValue();
                messageFrequence += (student.getName() + "\t\t");
                messageFrequence += (student.getId() + "\t\t");
                messageFrequence += ((student.getInTime() == null ? "-" : student.getInTime().format(Util.TIME_FORMATTER)) + "\t\t");
                messageFrequence += ((student.getOutTime() == null ? "-" : student.getOutTime().format(Util.TIME_FORMATTER)) + "\t\t");
                messageFrequence += (Util.formatDuration(student.getPermanenceTime()) + "\t\t");
                messageFrequence += (Util.formatDuration(student.getOutsideTime()) + "\t\t");
                messageFrequence += ((student.isPresent() ? "PRESENTE" : "AUSENTE") + "\t\t");
                messageFrequence += ((student.isValidPresence() ? "SIM" : "NÃO") + "\n");
            }*/

            //System.out.println(messageFrequence);


            GoogleMail.Send("ctrlassistant", "arrozdoce",
                    clazz.getDiscipline().getTeacher().getEmail(), "gustavo_votagus@hotmail.com", "Relatório de Frequência (Aula: "+clazz.getDiscipline().getName()+")", messageFrequence, "relatorio_de_frequencia.csv");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    public ArrayList<User> abrirArquivoCsvAutorizados (File file) {
        if (file == null) {
            return null;
        }

        BufferedReader bufferedReader = null;
        ArrayList<User> users = null;

        try {

            bufferedReader = new BufferedReader(new FileReader(file));

            users = new ArrayList<>();
            String line;

            System.out.println("Alunos autorizados:");

            while ((line = bufferedReader.readLine()) != null) {
                String[] lineSplited = line.split(COMMA_DELIMITER);
                User user = new User(lineSplited[0], lineSplited[1]);
                users.add(user);
                System.out.println("MATRÍCULA: [" + user.getMatricula() + "] UID: [" + user.getTag() + "]");
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
            return users;
        }
    }
    */

}
