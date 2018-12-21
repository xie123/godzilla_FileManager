package com.monster.godzilla.interfaces;

import com.monster.godzilla.target.Target;

import java.util.List;


public interface ICopyFiless {
	void start(List<String> files, String des);
	void cancel();
	void pause();
	void resume();
	boolean isCancel();
	boolean isRunning();
	boolean isPause();
	void registerCallback(Target.ReuestCopyImpl callback);
	void unregisterCallback(Target.ReuestCopyImpl callback);
}