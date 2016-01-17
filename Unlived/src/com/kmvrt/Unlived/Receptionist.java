package com.kmvrt.Unlived;

import com.kmvrt.Unlived.menu.Menu;
import com.kmvrt.Unlived.gameplay.Arena;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.Input.Keys;

public class Receptionist {
	// handle all the input
	
	private Manager manager;

	private int moveUpKey = Keys.UP;
	private int moveDownKey = Keys.DOWN;
	private int moveRightKey = Keys.RIGHT;
	private int moveLeftKey = Keys.LEFT;
	private int shootRightKey = Keys.D;
	private int shootLeftKey = Keys.S;
	private int bodySwapKey = Keys.A;
	private int backKey = Keys.ESCAPE;

	private boolean moveUp;
	private boolean moveDown;
	private boolean moveRight;
	private boolean moveLeft;
	private boolean shootRight;
	private boolean shootLeft;
	private boolean bodySwap;
	private boolean back;
	
	private boolean justMoveUp;
	private boolean justMoveDown;
	private boolean justMoveRight;
	private boolean justMoveLeft;
	private boolean justShootRight;
	private boolean justShootLeft;
	private boolean justBodySwap;
	private boolean justBack;


// constructor -------------------------------------------------------------------------------------------------
	public Receptionist(Manager manager) {
	
		this.manager = manager;
		reset();
	}

	private void reset() {
		
		moveUp = false;
		moveDown = false;
		moveRight = false;
		moveLeft = false;
		shootRight = false;
		shootLeft = false;
		bodySwap = false;
		back = false;
	}


// poll the input ----------------------------------------------------------------------------------------------
	public void pollInput() {
	
		reset();
		if(Gdx.app.getType() == Application.ApplicationType.Desktop) {
			pollKeyboard();
		}
	}

	private void pollKeyboard() {

		if(manager.getScreen() instanceof Menu) {
			pollMenuKeyboard();
		} else if(manager.getScreen() instanceof Arena) {
			pollArenaKeyboard();
		}
		
	}

	private void pollMenuKeyboard() {
	
		if(Gdx.input.isKeyPressed(moveUpKey)) {
			if(!justMoveUp) {
				moveUp = true;	
				justMoveUp = true;
				Timer.schedule(
					new Timer.Task() {
					
						@Override
						public void run() {
							
							justMoveUp = false;
						}
					}, Constants.POINTER_CON_MOVE_INTERVAL);
			}
			
		} else {
			justMoveUp = false;
		}

		if(Gdx.input.isKeyPressed(moveDownKey)) {
			if(!justMoveDown) {
				moveDown = true;
				justMoveDown = true;
				Timer.schedule(
					new Timer.Task() {
				
						@Override
						public void run() {
						
							justMoveDown = false;
						}
					}, Constants.POINTER_CON_MOVE_INTERVAL);
			}
			
		} else {
			justMoveDown = false;
		}

		if(Gdx.input.isKeyPressed(moveRightKey)) {
			if(!justMoveRight) {
				moveRight = true;
				justMoveRight = true;
				Timer.schedule(
					new Timer.Task() {
				
						@Override
						public void run() {
						
							justMoveRight = false;
						}
					}, Constants.POINTER_CON_MOVE_INTERVAL);
			}
			
		} else {
			justMoveRight = false;
		}

		if(Gdx.input.isKeyPressed(moveLeftKey)) {
			if(!justMoveLeft) {
				moveLeft = true;
				justMoveLeft = true;
				Timer.schedule(
					new Timer.Task() {
				
						@Override
						public void run() {
					
							justMoveLeft = false;
						}
					}, Constants.POINTER_CON_MOVE_INTERVAL);
			}
			
		} else {
			justMoveLeft = false;
		}

		if(Gdx.input.isKeyPressed(shootRightKey)) {
			if(!justShootRight) {
				shootRight = true;
				justShootRight = true;
			}

		} else {
			justShootRight = false;
		} 
	
		if(Gdx.input.isKeyPressed(backKey)) {
			if(!justBack) {
				back = true;
				justBack = true;
			}
		} else {
			justBack = false;
		}
	}

	private void pollArenaKeyboard() {
	
		if(Gdx.input.isKeyPressed(moveUpKey)) {
			moveUp = true;
		} 
		if(Gdx.input.isKeyPressed(moveDownKey)) {
			moveDown = true;
		}
		if(Gdx.input.isKeyPressed(moveRightKey)) {
			moveRight = true;
		} 
		if(Gdx.input.isKeyPressed(moveLeftKey)) {
			moveLeft = true;
		}

		if(Gdx.input.isKeyPressed(shootRightKey)) {
			shootRight = true;
		} 
		if(Gdx.input.isKeyPressed(shootLeftKey)) {
			shootLeft = true;
		} 
		
		if(Gdx.input.isKeyPressed(bodySwapKey)) {
			if(!justBodySwap) {
				bodySwap = true;
				justBodySwap = true;
			}
		} else {
			justBodySwap = false;
		}

		if(Gdx.input.isKeyPressed(backKey)) {
			if(!justBack) {
				back = true;
				justBack = true;
			}
		} else {
			justBack = false;
		}
	}



// getters -------------------------------------------------------------------------------------------------
	public boolean moveUp() {
		return moveUp;
	}

	public boolean moveDown() {
		return moveDown;
	}

	public boolean moveRight() {
		return moveRight;
	}
		
	public boolean moveLeft() {
		return moveLeft;
	}

	public boolean shootRight() {
		return shootRight;
	}

	public boolean shootLeft() {
		return shootLeft;
	}

	public boolean bodySwap() {
		return bodySwap;
	}

	public boolean back() {
		return back;
	}

}
