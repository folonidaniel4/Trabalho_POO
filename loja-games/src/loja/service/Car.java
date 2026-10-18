package loja.service;

import loja.model.podruto;

import java.util.LinkedHashMap;
import java.util.Map;

public class Car {
    private final Map<podruto, Integer> itens = new LinkedHashMap<>();


    public void adicionarItem(podruto podruto, int quantidade){
        if(quantidade <= 0){
            throw new IllegalArgumentException("Você deve adicionar ao menos um item no carrinho para proseeguir com a operação");
        }

        itens.merge(podruto, quantidade, Integer::sum);
    }
}
