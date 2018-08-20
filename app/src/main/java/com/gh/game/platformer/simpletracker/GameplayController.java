package com.gh.game.platformer.simpletracker;

import com.gh.game.platformer.simpletracker.ControlAreaManager.TouchEventResult;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static android.graphics.Rect.intersects;
import static com.gh.game.platformer.simpletracker.GlobalGameInfo.getInfo;

public class GameplayController {
    /* once every period increase speed and other game difficulty changes */
    private static final int GAME_SPEED_INCREASE_PERIOD = 20 * 1000;
    private static final int DEFAULT_ENEMY_SPEED = 3;

    /* no of interactions for the ship to be collide immune after a previous collision */
    private static final int SHIP_COLLIDE_IMMUNITY = 1000;
    private boolean shipCollided;
    private int shipCollidedCounter;

    //higher chance to get enemy to shop than enemy to hit(asteroid)
    private EnemyInfo[] allEnemies = {EnemyInfo.OCTOPUS, EnemyInfo.OCTOPUS, EnemyInfo.OCTOPUS, EnemyInfo.OCTOPUS, EnemyInfo.OCTOPUS_SMALL, EnemyInfo.OCTOPUS_SMALL, EnemyInfo.BONUS_ASTEROID};
    private ShipInfo[] allShips = {ShipInfo.STINGRAY, ShipInfo.SEA_URCHIN, ShipInfo.HAMMERHEAD};

    private GameShip gameShip;
    private Queue<Enemy> enemies;
    private Queue<Bullet> bullets;
    private Score score;

    private int currentIteration;
    private int currentEnemySpeed = DEFAULT_ENEMY_SPEED;
    private int currentShipIndex;
    private int currentMaxNoOfBullets;
    private int scoreToSwitchShipAt;

    private int controlAreaRadius;

    public GameplayController(int controlAreaRadius, Score score) {
        this.controlAreaRadius = controlAreaRadius;
        this.score = score;
        init();
    }

    protected void init() {
        chooseNewShip();
        enemies = new ConcurrentLinkedQueue<>();
        bullets = new ConcurrentLinkedQueue<>();
    }

    public List<DrawableGameElement> getAllDrawables() {
        List<DrawableGameElement> allDrawables = new ArrayList<>();
        allDrawables.addAll(bullets);
        allDrawables.addAll(enemies);
        allDrawables.add(gameShip);
        allDrawables.add(score);

        return allDrawables;
    }

    public void fireButtonPressed() {
        if (bullets.size() < currentMaxNoOfBullets) {
            if (gameShip.getElementId() == R.drawable.ship_4_hammerhead) {
                bullets.add(new Bullet(gameShip.getX() + 30, gameShip.getY()));
                bullets.add(new Bullet(gameShip.getX() + gameShip.getWidth() - 30, gameShip.getY()));
            } else {
                bullets.add(new Bullet(gameShip.getX() + gameShip.getWidth() / 2, gameShip.getY()));
            }
        }
    }

    public void spawnEnemy() {
        int enemyListIdx = (int)(allEnemies.length * Math.random());
        Enemy enemy = new Enemy(0, 0, allEnemies[enemyListIdx].getElementId(), currentEnemySpeed, allEnemies[enemyListIdx].getValuedAt());
        enemy.setX((float)(enemy.getWidth() + (getInfo().getScreenWidth() - enemy.getWidth() * 2) * Math.random()) - enemy.getWidth());
        enemies.add(enemy);
    }

    public void doGameElementsInteractions(TouchEventResult touchEventResult) {
        ++currentIteration;
        if (currentIteration % GAME_SPEED_INCREASE_PERIOD == 0) {
            ++currentEnemySpeed;
            if (currentEnemySpeed > 99) {
                currentEnemySpeed = DEFAULT_ENEMY_SPEED;
            }
            System.out.println(" *** enemy speed changed: " + currentEnemySpeed);
        }

        //ship immunity counting
        if (shipCollided) {
            ++shipCollidedCounter;
            if (shipCollidedCounter >= SHIP_COLLIDE_IMMUNITY) {
                shipCollided = false;
                shipCollidedCounter = 0;
            }
        }

        // 1 move ship
        gameShip.move(touchEventResult, null);

        // 2 move enemies
        Iterator<Enemy> iteratorEnemies = enemies.iterator();
        while(iteratorEnemies.hasNext()) {
            Enemy currentEnemy = iteratorEnemies.next();
            //escaped enemies have negative score value
            if (currentEnemy.move(touchEventResult, iteratorEnemies)) {
                updateScoreWith(-currentEnemy.getValue());
            }
        }
        // 3 move bullets
        Iterator<Bullet> iteratorBullets = bullets.iterator();
        while(iteratorBullets.hasNext()) {
            iteratorBullets.next().move(touchEventResult, iteratorBullets);
        }

        // 4 check if any of the enemies hit any of the bullets or the ship
        iteratorEnemies = enemies.iterator();
        while(iteratorEnemies.hasNext()) {
            DrawableGameElement enemy = iteratorEnemies.next();
            if (!shipCollided && intersects(gameShip.toRect(), enemy.toRect())) {
                doEnemyHitShip(enemy, iteratorEnemies);
                continue;
            }
            iteratorBullets = bullets.iterator();
            while(iteratorBullets.hasNext()) {
                Bullet bullet = iteratorBullets.next();
                if (intersects(bullet.toRect(), enemy.toRect())) {
                    doBulletHitEnemy(bullet, enemy, iteratorBullets, iteratorEnemies);
                    break;
                }
            }
        }
    }

    private void chooseNewShip() {
        ShipInfo shipInfo = allShips[currentShipIndex++ % allShips.length];
        if (gameShip == null) {
            gameShip = new GameShip(getInfo().getScreenWidth() / 2, getInfo().getScreenHeight(), shipInfo.getElementId());
        } else {
            gameShip = new GameShip(gameShip.getX(), gameShip.getY(), shipInfo.getElementId());
        }
        currentMaxNoOfBullets = shipInfo.getMaxNoOfBullets();
        scoreToSwitchShipAt =  shipInfo.getScoreToSwitchAt();
        gameShip.setControlAreaRadius(controlAreaRadius);
        System.out.println(" +++ ship changed: " + gameShip.getElementId());
    }

    private void doEnemyHitShip(DrawableGameElement enemy, Iterator<? extends DrawableGameElement> iteratorEnemies) {
        System.out.println("Ship [" + gameShip + "] hit enemy [" + enemy + "]");
        shipCollided = true;
        iteratorEnemies.remove();
        updateScoreWith(-((Scorable)enemy).getValue());
    }

    private void doBulletHitEnemy(DrawableGameElement bullet, DrawableGameElement enemy, Iterator<? extends DrawableGameElement> iteratorBullets, Iterator<? extends DrawableGameElement> iteratorEnemies) {
        System.out.println("Bullet [" + bullet + "] hit enemy [" + enemy + "]");
        iteratorBullets.remove();
        iteratorEnemies.remove();
        updateScoreWith(((Scorable)enemy).getValue());
    }

    private void updateScoreWith(int value) {
        score.updateScoreWith(value);
        if (score.getScore() >= scoreToSwitchShipAt) {
            chooseNewShip();
        }
    }

    //=============================//

    private enum ShipInfo {
        STINGRAY(R.drawable.ship_3_stingray, 5, 200),
        SEA_URCHIN(R.drawable.ship_5_seaurchin, 10, 1200),
        HAMMERHEAD(R.drawable.ship_4_hammerhead, 20, 10000);

        private int elementId;
        private int maxNoOfBullets;
        private int scoreToSwitchAt;

        ShipInfo(int elementId, int maxNoOfBullets, int scoreToSwitchAt) {
            this.elementId = elementId;
            this.maxNoOfBullets = maxNoOfBullets;
            this.scoreToSwitchAt = scoreToSwitchAt;
        }

        public int getElementId() {
            return elementId;
        }

        public int getMaxNoOfBullets() {
            return maxNoOfBullets;
        }

        public int getScoreToSwitchAt() {
            return scoreToSwitchAt;
        }
    }

    private enum EnemyInfo {
        OCTOPUS(R.drawable.enemy_2_octopus, 1),
        OCTOPUS_SMALL(R.drawable.enemy_3_octopussmall, 3),
        //negative score: shooting it takes away score, hitting it gives score
        BONUS_ASTEROID(R.drawable.bonus_1_asteroid, -5);

        private int elementId;
        private int valuedAt;

        EnemyInfo(int elementId, int valuedAt) {
            this.elementId = elementId;
            this.valuedAt = valuedAt;
        }

        public int getElementId() {
            return elementId;
        }

        public int getValuedAt() {
            return valuedAt;
        }
    }
}
