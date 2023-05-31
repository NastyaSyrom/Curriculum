package DAO;

import DAO.TaskDAO;
import model.Plan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс TaskDAOImpl представляет собой реализацию интерфейса TaskDAO и взаимодействует с базой данных
 */
public class TaskDAOImpl implements TaskDAO {
    private Connection conn;

    /**
     * конструктор класса TaskDAOImpl, который реализует интерфейс DAO (Data Access Object) для работы с объектами типа Task в базе данных.
     * Конструктор устанавливает соединение с базой данных H2, используя JDBC драйвер, и сохраняет это соединение в переменной conn.
     */
    public TaskDAOImpl() {

        try {
            conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
            //conn.prepareStatement("RUNSCRIPT FROM 'sql.sql'").execute();
            //conn.prepareStatement(" DROP ALL OBJECTS DELETE FILES").execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод getAllPlans(). Данный метод предназначен для получения списка полного из таблицы "plan".
     * Этот метод использует объект PreparedStatement для выполнения запроса "SELECT * FROM plan" и возвращает список объектов Plan, содержащих данные из таблицы.
     * @return
     */
    @Override
    public List<Plan> getAllPlans() {
        List<Plan> plans = new ArrayList<>();

        try {
            PreparedStatement ps = conn.prepareStatement("SELECT Studgroup.code_Studgroup, Discipline.name AS discipline_name, Discipline.semester, FormOfTraining.name AS form_of_training, AttestationForm.name as test FROM Discipline\n" +
                    "JOIN Studgroup ON Studgroup.faculty_id = Discipline.faculty_id AND Studgroup.educationalProgram_id = Discipline.formOfTraining_id\n" +
                    "JOIN FormOfTraining ON FormOfTraining.id = Discipline.formOfTraining_id\n" +
                    "JOIN AttestationForm ON AttestationForm.id = Discipline.attestationForm_id\n" +
                    "ORDER BY Discipline.semester;");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Plan plan = new Plan(
                        rs.getString(1),
                        rs.getString(4),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(5)
                );

                plans.add(plan);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plans;
    }

    /**
     * Метод updatePlans(String groupName, String Simestr, String eduForm) Данный метод предназначен для обновления списка планов на основе переданных параметров.
     * Метод принимает три параметра, содержащие ID группы, номер семестра и форму обучения.
     * @param groupId
     * @param semester
     * @param eduForm
     * @return
     */
    @Override
    public List<Plan> updatePlans(String groupId, String semester, String eduForm) {

        List<Plan> plans = new ArrayList<>();
        PreparedStatement ps = null;

        StringBuilder queryBuilder = new StringBuilder("SELECT Studgroup.code_Studgroup, Discipline.name AS discipline_name, Discipline.semester, FormOfTraining.name AS form_of_training, AttestationForm.name as test FROM Discipline\n" +
                "JOIN Studgroup ON Studgroup.faculty_id = Discipline.faculty_id AND Studgroup.educationalProgram_id = Discipline.formOfTraining_id\n" +
                "JOIN FormOfTraining ON FormOfTraining.id = Discipline.formOfTraining_id\n" +
                "JOIN AttestationForm ON AttestationForm.id = Discipline.attestationForm_id\n" +
                "WHERE ");

        if(groupId != null){
            queryBuilder.append("Studgroup.id = '").append(groupId).append("' ");
        }

        if(semester != null){
            if(groupId != null){
                queryBuilder.append("AND ");
            }
            queryBuilder.append("Discipline.semester = " + semester);
        }

        if(eduForm != null){
            if(semester != null || groupId != null){
                queryBuilder.append("AND ");
            }
            queryBuilder.append("Discipline.formOfTraining_id = " + eduForm);
        }

        try {
            ps = conn.prepareStatement(String.valueOf(queryBuilder));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Plan plan = new Plan(
                        rs.getString(1),
                        rs.getString(4),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(5)
                );


                plans.add(plan);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plans;
    }

    /**
     * Метод addNewPlan() предназначен для добавления новой записи в таблицу DISCIPLINE базы данных.
     * Метод получает на вход параметры добавляемой записи, которые используются для формирования SQL-запроса.
     * @param planName
     * @param semester
     * @param attestationFormId
     * @param formOfTrainingId
     * @param facultyId
     */
    @Override
    public void addNewPlan(String planName, String semester, String attestationFormId, String formOfTrainingId, String facultyId) {
            try {
                conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
                PreparedStatement ps = conn.prepareStatement("INSERT INTO DISCIPLINE(name, semester, attestationForm_id, formOfTraining_id, faculty_id) values\n" +
                        "('" + planName + "', " + Integer.parseInt(semester) + ", " + Integer.parseInt(attestationFormId) + ", " + Integer.parseInt(formOfTrainingId) + ", " + Integer.parseInt(facultyId) + ");");
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Новая дисцирлина успешно добавлена!");
    }

    /**
     * Метод deletePlan(). Данный метод предназначен для удаления записи из таблицы DISCIPLINE базы данных.
     * Метод получает на вход объект типа Plan, который содержит информацию об удаляемой записи.
     * Для выполнения операции удаления метод использует объект PreparedStatement, в котором задается SQL-запрос.
     * @param plan
     */
    @Override
    public void deletePlan(Plan plan) {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Discipline WHERE name = ? \n" +
                    "and semester = ? \n" +
                    "and attestationForm_id = (SELECT id FROM AttestationForm WHERE name = ?) \n" +
                    "and formOfTraining_id = (SELECT id FROM FormOfTraining WHERE name = ?) \n" +
                    "and faculty_id = (SELECT faculty_id FROM STUDGROUP s, Faculty f WHERE s.faculty_id = f.id AND s.code_Studgroup = ?);");
            ps.setString(5, plan.getGroupName());
            ps.setString(4, plan.getFormOfEducation());
            ps.setString(1, plan.getSubject());
            ps.setString(2, plan.getSimest());
            ps.setString(3, plan.getFormOfAttestation());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}