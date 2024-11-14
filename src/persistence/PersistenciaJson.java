package persistence;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Medico;
import model.Paciente;
import model.Usuario;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaJson {
    private static final String PATH_PACIENTES = "C:" + File.separator + "Users" + File.separator + "luana" + File.separator + "Persistencia" + File.separator + "pacientes.txt";
    private static final String PATH_MEDICOS = "C:" + File.separator + "Users" + File.separator + "luana" + File.separator + "Persistencia" + File.separator + "medicos.txt";
    private static final String PATH_USUARIOS = "C:" + File.separator + "Users" + File.separator + "luana" + File.separator + "Persistencia" + File.separator + "usuarios.txt";

    private static final Gson gson = new Gson();

    public static void salvarMedicos(List<Medico> medicos) {
        try (FileWriter writer = new FileWriter(PATH_MEDICOS)) {
            gson.toJson(medicos, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Medico> carregarMedicos() {
        try (FileReader reader = new FileReader(PATH_MEDICOS)) {
            return gson.fromJson(reader, new TypeToken<List<Medico>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void salvarPacientes(List<Paciente> pacientes) {
        try (FileWriter writer = new FileWriter(PATH_PACIENTES)) {
            gson.toJson(pacientes, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Paciente> carregarPacientes() {
        try (FileReader reader = new FileReader(PATH_PACIENTES)) {
            return gson.fromJson(reader, new TypeToken<List<Paciente>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void salvarUsuarios(List<Usuario> usuarios) {
        try (FileWriter writer = new FileWriter(PATH_USUARIOS)) {
            gson.toJson(usuarios, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para carregar os usuários do arquivo JSON
    public static List<Usuario> carregarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();  // Inicializa a lista para garantir que não seja null
        try (FileReader reader = new FileReader(PATH_USUARIOS)) {
            // Tenta desserializar os usuários a partir do arquivo
            usuarios = gson.fromJson(reader, new TypeToken<List<Usuario>>(){}.getType());
            if (usuarios == null) {
                usuarios = new ArrayList<>();  // Se a lista estiver nula, inicializa como vazia
            }
        } catch (IOException e) {
            // Exceção em caso de erro no arquivo, lista vazia é retornada
            e.printStackTrace();
        }
        return usuarios;
    }

    //Sem JSON
    /*// Método para carregar os usuários do arquivo
    public static List<Usuario> carregarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("usuarios.txt"))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(",");
                usuarios.add(new Usuario(dados[0], dados[1], dados[2], dados[3]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    // Método para salvar usuários no arquivo
    public static void salvarUsuarios(List<Usuario> usuarios) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("usuarios.txt"))) {
            for (Usuario u : usuarios) {
                writer.write(u.getNome() + "," + u.getUsuario() + "," + u.getSenha() + "," + u.getTipo() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
