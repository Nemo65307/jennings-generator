package com.nemo.jennings.dao;

import com.nemo.jennings.dto.PolynomialDTO;

import java.util.List;

public interface PolynomialDAO {

    List<PolynomialDTO> loadAll();

    List<PolynomialDTO> loadByN(short n);

    List<PolynomialDTO> loadByMaxN(short n);

    List<PolynomialDTO> loadByMinN(short n);

    List<PolynomialDTO> loadBetweenN(short min, short max);

    List<PolynomialDTO> loadInN(short n1, short n2, short n3);

}
