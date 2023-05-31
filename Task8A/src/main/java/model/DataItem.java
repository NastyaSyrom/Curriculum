package model;

/**
 * Класс DataItem представляет объект, который используется для  выпадающих списков.
 */
public class DataItem {
    private String displayMember;
    private String valueMember;

    /**
     * Конструктор принимает два параметра name и valueMember, которые инициализируют поля displayMember и valueMember соответственно.
     * @param name
     * @param valueMember
     */
    public DataItem(String name, String valueMember) {
        this.displayMember = name;
        this.valueMember = valueMember;
    }

    /**
     * Метод getDisplayMember() возвращает отображаемое значение объекта DataItem.
     * @return
     */
    public String getDisplayMember() {
        return displayMember;
    }

    /**
     * Метод getValueMember() возвращает реальное значениe объекта DataItem.
     * @return
     */
    public String getValueMember() {
        return valueMember;
    }
}
