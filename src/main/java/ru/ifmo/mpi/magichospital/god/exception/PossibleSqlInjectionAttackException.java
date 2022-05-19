package ru.ifmo.mpi.magichospital.god.exception;

public class PossibleSqlInjectionAttackException extends Exception {

	private static final long serialVersionUID = -8294186439649061305L;

	public PossibleSqlInjectionAttackException(String message) {
		super(message);
	}
	
}
