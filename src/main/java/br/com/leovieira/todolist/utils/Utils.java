/*
 * Esta classe foi criada para resolver o problema relacionado ao método de Update das tasks, pois ao fazer o update de propriedades das tasks, o valores que não eram enviados nas requisições do update assumiam o valor null. Com a implementação desta classe, há mais dinamismo no método update(alteração) das tasks, pois permite que cada propriedade seja alterada de forma individual, mantendo os valores das pripriedades das quais nenhuma alteração foi requisitada.
 */

package br.com.leovieira.todolist.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import br.com.leovieira.todolist.task.TaskModel;

public class Utils {

    //Método que copia todos os valores de propriedades não nulas
    public static void getNonNullProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertieNames(source));

    }


    //Método que armazena os nomes de todas propriedades com valores null
    public static String[] getNullPropertieNames(Object source) {

        final BeanWrapper src = new BeanWrapperImpl(source); // BeanWrapper => Interface para acessar as propriedades de objetos | BeanWrapperImpl => Implementação da interface.

        
        PropertyDescriptor[] pds = src.getPropertyDescriptors();//Obtendo e salvando em um Array todas as propriedades do objeto source.
        Set<String> emptyNames = new HashSet<>(); //Conjunto dos nomes de propriedades com valores nulos.

        for (PropertyDescriptor pd: pds) {
            Object srcValue = src.getPropertyValue(pd.getName()); //Para cada valor de propriedade. GetName

            if(srcValue == null) { //Se a propriedade for null
                emptyNames.add(pd.getName()); //Adicionar o nome da propriedade dentro do conjunto "emptyNames".

            } 
        }

        String[] result = new String[emptyNames.size()]; //instancia o conjunto "result" com uma lista que terá o tanhado de "emptyNames". 
        return emptyNames.toArray(result); //Converte o conjunto result em um Array de Strings
    }


    public static void copyNonNullProperties(TaskModel taskModel, TaskModel task) {
    }
    
}
