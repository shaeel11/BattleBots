package bots;

import java.awt.Graphics;
import java.awt.Image;

import arena.BattleBotArena;
import arena.BotInfo;
import arena.Bullet;

public class AyeLaMEOWBot extends Bot {

	/* AyeLaMEOWBot V1.1 (To Do's)
	 * Attempt to make the Bots dodge bullets
	 */

	double systemTime = System.currentTimeMillis();
	Image up, down, left, right, current; // images;
	private int move;
	private double x, y;
	BotHelper helper = new BotHelper(); // instance caller
	BotInfo me;
	Bullet bullet;

	public AyeLaMEOWBot() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void newRound() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets) {
		try {			
			BotInfo ally = helper.findClosest(_me, _bots);
			
			this.me = me;
			//System.out.println(me.getX());
			//System.out.println(me.getY());
			
			// make sure bullets exist
			if (bullets.length > 0) {
				// find closest Bullet
				Bullet closestDanger = helper.findClosest(me, bullets);
				// figuring out wither it's moving towards you or not (Negative when im shooting)
				if((Math.abs(me.getX()-closestDanger.getX())< Bot.RADIUS)){
					System.out.println(closestDanger.getYSpeed());
					if(closestDanger.getY() > me.getY()) {
						//BELOW ME
						if(closestDanger.getYSpeed() < 0) { //might need to switch sign
							//return right for now
							current = right;
							return BattleBotArena.RIGHT;
						}
					}
					else if(closestDanger.getY() < me.getY()) {
						//ABOVE ME
						if(closestDanger.getYSpeed() > 0) {
							//return right for now
							current = right;
							return BattleBotArena.RIGHT;
						}
					}
				}
				else if ((Math.abs(me.getY()-closestDanger.getY())< Bot.RADIUS)) {
					System.out.println(closestDanger.getXSpeed());
					if(closestDanger.getX() > me.getX()) {
						//RIGHT OF ME
						if(closestDanger.getXSpeed() < 0) { //might need to switch sign
							//return up for now
							current = up;
							return BattleBotArena.UP;
						}
					}
					else if(closestDanger.getX() < me. getX()) {
						//LEFT OF ME
						if(closestDanger.getXSpeed() > 0) {
							//return up for now
							current = up;
							return BattleBotArena.UP;
						}
					}
				}
			}
			
			BotInfo closestEnemy = helper.findClosest(me, liveBots);
			
			if(me.getX() == x && me.getY() == y) {
				if (move == BattleBotArena.UP) {
					move = BattleBotArena.DOWN;
				}
				else if (move == BattleBotArena.LEFT) {
					move = BattleBotArena.RIGHT;
				}
				else if (move == BattleBotArena.DOWN) {
					move = BattleBotArena.UP;
				}
				else if (move == BattleBotArena.LEFT) {
					move = BattleBotArena.UP;
				}
				return move;
			}
			
			x = me.getX();
			y = me.getY();
			
			
			if (liveBots.length > 0 && deadBots.length > 0) {
				System.out.println("l and d");
				//closestEnemy 
				BotInfo closestDead = helper.findClosest(me, deadBots);
				// find distances of closest Enemy & Dead
				double distDead = Math.abs(helper.calcDistance(me.getX(), me.getY(), closestDead.getX(), closestDead.getY()));
				double distEnemy = Math.abs(helper.calcDistance(me.getX(), me.getY(), closestEnemy.getX(), closestEnemy.getY()));
				//choose between fighting enemy or getting ammo based on distance.
				//Unless we have none or close to no bullets left
				
				//focus alive if closer
				if (distEnemy <= distDead && me.getBulletsLeft() > 5 || me.getBulletsLeft() > 5) {
					//displacement of horizontal distance
					double horizDist = helper.calcDisplacement(me.getX(), closestEnemy.getX());
					//displacement of vertical distance
					double verticDist = helper.calcDisplacement(me.getY(), closestEnemy.getY());
					
					//priorities on distance
					if(horizDist <= verticDist) {
						//System.out.println("h");
						//Horizontal
						//if on it's on the same X
						if(Math.abs(me.getX()-closestEnemy.getX())< Bot.RADIUS) {
							//shoot bullet
							//shoot bullet depending from up or down
							if(closestEnemy.getY() > me.getY()) {
							return BattleBotArena.FIREDOWN;
							}
							else if(closestEnemy.getY() < me.getY()) {
							return BattleBotArena.FIREUP;
							}	
						}
						// move left if it's to the left
						else if(closestEnemy.getX() < me.getX()) {
							current = left;
							return BattleBotArena.LEFT;
						} 
						// move right if it's to the right
						else if (closestEnemy.getX() > me.getX()) {
							current = right;
							return BattleBotArena.RIGHT;
						}
					}
					
					else if(verticDist < horizDist) {
						//System.out.println("v");
						//Vertical
						//if on it's on the same Y
						if(Math.abs(me.getY()-closestEnemy.getY())< Bot.RADIUS) {
							//shoot bullet depending from left or right
							if(closestEnemy.getX() > me.getX()) {
							return BattleBotArena.FIRERIGHT;
							}
							else if(closestEnemy.getX() < me.getX()) {
							return BattleBotArena.FIRELEFT;
							}
						}
						//move upward if it's above
						else if (closestEnemy.getY() < me.getY()) {
							current = up;
							return BattleBotArena.UP;
						}//move downward if it's below
						else if (closestEnemy.getY() > me.getY()){
							current = down;
							return BattleBotArena.DOWN;
						}
					}
				}
				//focus dead if closer
				else if (distDead < distEnemy || me.getBulletsLeft() <= 5) {
					
					//Horizontal
					// move left if it's to the left
					if(closestDead.getX() < me.getX()) {
						current = left;
						return BattleBotArena.LEFT;
					} 
					// move right if it's to the right
					else if (closestDead.getX() > me.getX()) {
						current = right;
						return BattleBotArena.RIGHT;
					}
					
					//Vertical
					//move upward if it's above
					if (closestDead.getY() < me.getY()) {
						current = up;
						return BattleBotArena.UP;
					}//move downward if it's below
					else if (closestDead.getY() > me.getY()){
						current = down;
						return BattleBotArena.DOWN;
					}
				}
			}
			
			else if(liveBots.length > 0) {
				//displacement of horizontal distance
				double horizDist = Math.abs(helper.calcDisplacement(me.getX(), closestEnemy.getX()));
				//displacement of vertical distance
				double verticDist = Math.abs(helper.calcDisplacement(me.getY(), closestEnemy.getY()));
				//System.out.println(verticDist);
				//priorities on distance
				if(horizDist <= verticDist) {
					//System.out.println("h");
					//Horizontal
					//if on it's on the same X
					if(Math.abs(me.getX()-closestEnemy.getX())< Bot.RADIUS) {
						//shoot bullet
						//shoot bullet depending from up or down
						if(closestEnemy.getY() > me.getY()) {
						return BattleBotArena.FIREDOWN;
						}
						else if(closestEnemy.getY() < me.getY()) {
						return BattleBotArena.FIREUP;
						}	
					}
					// move left if it's to the left
					else if(closestEnemy.getX() < me.getX()) {
						current = left;
						return BattleBotArena.LEFT;
					} 
					// move right if it's to the right
					else if (closestEnemy.getX() > me.getX()) {
						current = right;
						return BattleBotArena.RIGHT;
					}
				}
				
				else if(verticDist < horizDist) {
					//System.out.println("v");
					//Vertical
					//if on it's on the same Y
					if(Math.abs(me.getY()-closestEnemy.getY())< Bot.RADIUS) {
						//shoot bullet depending from left or right
						if(closestEnemy.getX() > me.getX()) {
						return BattleBotArena.FIRERIGHT;
						}
						else if(closestEnemy.getX() < me.getX()) {
						return BattleBotArena.FIRELEFT;
						}
					}
					//move upward if it's above
					else if (closestEnemy.getY() < me.getY()) {
						current = up;
						return BattleBotArena.UP;
					}//move downward if it's below
					else if (closestEnemy.getY() > me.getY()){
						current = down;
						return BattleBotArena.DOWN;
					}
				}
			}

			/* maybe can be used for ignoring dead bodies with no ammo??
			 * if(closestDead.getBulletsLeft() == 0) { closestDead. }
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}
		return BattleBotArena.DOWN;
	}
	
	@Override
	public void draw(Graphics g, int x, int y) {
		// TODO Auto-generated method stub
		g.drawImage(current, x, y, Bot.RADIUS * 2, Bot.RADIUS * 2, null);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTeamName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String outgoingMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void incomingMessage(int botNum, String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public String[] imageNames() {
		// TODO Auto-generated method stub
		String[] images = { "umu.jpg", "umu.jpg", "umu.jpg", "umu.jpg" };
		return images;
	}

	@Override
	public void loadedImages(Image[] images) {
		// TODO Auto-generated method stub
		if (images != null) {
			current = up = images[0];
			down = images[1];
			left = images[2];
			right = images[3];
		}
	}

}
