package ru.ifmo.mpi.magichospital.god.exception;

public class PrayerAlreadyAnsweredException extends Exception {
	
	private static final long serialVersionUID = -2931880271180754894L;

	public PrayerAlreadyAnsweredException(String message) {
		super(message);
	}

}
