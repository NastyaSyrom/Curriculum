package DAO;

import model.Plan;

import java.util.List;

/**
 * Интерфейс TaskDAO для взаимодействия с базой данных учебных планов.
 */
public interface TaskDAO {
    /**
     * метод, который возвращает список всех планов учебных планов.
     * @return
     */
    List<Plan> getAllPlans();

    /**
     * метод, который обновляет список учебных планов
     * @param groupName
     * @param Simestr
     * @param eduForm
     * @return
     */

    List<Plan> updatePlans(String groupName, String Simestr, String eduForm);

    /**
     * метод для добавления новой дисциплины в учебный плана
     * @param planName
     * @param semester
     * @param attestationFormId
     * @param formOfTrainingId
     * @param facultyId
     */

    void addNewPlan(String planName, String semester, String attestationFormId, String formOfTrainingId, String facultyId);

    /**
     * метод для удаления дисциплины из учебного плана
     * @param plan
     */
    void deletePlan(Plan plan);
}