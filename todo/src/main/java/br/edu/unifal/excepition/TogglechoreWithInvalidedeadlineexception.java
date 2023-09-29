package br.edu.unifal.excepition;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TogglechoreWithInvalidedeadlineexception extends RuntimeException{

    public TogglechoreWithInvalidedeadlineexception(String message){
        super(message);
    }
}
