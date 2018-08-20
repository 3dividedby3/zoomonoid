package com.gh.game.platformer.simpletracker;

import com.gh.game.platformer.simpletracker.ControlAreaManager.TouchEventResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//import static org.junit.Assert.assertTrue;


@RunWith(MockitoJUnitRunner.class)
public class GameAreaViewTest {
//
//    private GameAreaView gameAreaView;
//
//    @Mock
//    private GameShip mockGameShip;
//
//    @Mock
//    private TouchEventResult mockTouchEventResult;
//
//    @Mock
//    private ControlAreaManager mockControlAreaManager;
//
//
//    @Before
//    public void setup() {
//        when(mockGameShip.getX()).thenReturn(0f);
//        when(mockGameShip.getY()).thenReturn(0f);
//        when(mockGameShip.isBelowScreenHeight()).thenReturn(true);
//        when(mockGameShip.isUnderScreenWidth()).thenReturn(true);
//
//        when(mockControlAreaManager.getControlCenterX()).thenReturn(300);
//        when(mockControlAreaManager.getControlCenterY()).thenReturn(300);
//        when(mockControlAreaManager.getRadius()).thenReturn(100);
//
//        gameAreaView = new GameAreaView(mockGameShip, mockControlAreaManager);
//        testShipController.setLogger(mockLogger);
//    }
//
//    @Test
//    public void testCannotGoBelowX0() {
//        when(mockTouchEventResult.isInsideControlArea()).thenReturn(true);
//        when(mockTouchEventResult.getOffsetX()).thenReturn(-70);
//        when(mockTouchEventResult.getOffsetY()).thenReturn(10);
//        for (int cycles = 0; cycles < 100; ++cycles ) {
//            testShipController.updateStatus(mockTouchEventResult);
//        }
//
//        verify(mockGameShip, times(0)).setX(any(Integer.class));
//
//        //reverse
//        when(mockTouchEventResult.getOffsetX()).thenReturn(10);
//        for (int cycles = 0; cycles < 100; ++cycles ) {
//            testShipController.updateStatus(mockTouchEventResult);
//        }
//
//        verify(mockGameShip, times(100)).setX(any(Integer.class));
//    }
//
//    @Test
//    public void testCannotGoAboveMaxWidth() {
//        when(mockTouchEventResult.isInsideControlArea()).thenReturn(true);
//        when(mockTouchEventResult.getOffsetX()).thenReturn(10);
//        when(mockTouchEventResult.getOffsetY()).thenReturn(10);
//
//        when(mockGameShip.getX()).thenReturn(10f);
//        when(mockGameShip.isUnderScreenWidth()).thenReturn(false);
//
//        for (int cycles = 0; cycles < 100; ++cycles ) {
//            testShipController.updateStatus(mockTouchEventResult);
//        }
//
//        verify(mockGameShip, times(0)).setX(any(Integer.class));
//
//        //reverse
//        when(mockTouchEventResult.getOffsetX()).thenReturn(-10);
//        for (int cycles = 0; cycles < 100; ++cycles ) {
//            testShipController.updateStatus(mockTouchEventResult);
//        }
//
//        verify(mockGameShip, times(100)).setX(any(Integer.class));
//    }
//
//    @Test
//    public void testCannotGoBelowY0() {
//        when(mockTouchEventResult.isInsideControlArea()).thenReturn(true);
//        when(mockTouchEventResult.getOffsetX()).thenReturn(10);
//        when(mockTouchEventResult.getOffsetY()).thenReturn(-10);
//        for (int cycles = 0; cycles < 100; ++cycles ) {
//            testShipController.updateStatus(mockTouchEventResult);
//        }
//
//        verify(mockGameShip, times(0)).setY(any(Integer.class));
//
//        //reverse
//        when(mockTouchEventResult.getOffsetY()).thenReturn(10);
//        for (int cycles = 0; cycles < 100; ++cycles ) {
//            testShipController.updateStatus(mockTouchEventResult);
//        }
//
//        verify(mockGameShip, times(100)).setY(any(Integer.class));
//    }
//
//    @Test
//    public void testCannotGoAboveMaxHeight() {
//        when(mockGameShip.getY()).thenReturn(10f);
//        when(mockGameShip.isBelowScreenHeight()).thenReturn(false);
//
//        when(mockTouchEventResult.isInsideControlArea()).thenReturn(true);
//        when(mockTouchEventResult.getOffsetX()).thenReturn(10);
//        when(mockTouchEventResult.getOffsetY()).thenReturn(10);
//        for (int cycles = 0; cycles < 100; ++cycles ) {
//            testShipController.updateStatus(mockTouchEventResult);
//        }
//
//        verify(mockGameShip, times(0)).setY(any(Integer.class));
//
//        //reverse
//        when(mockTouchEventResult.getOffsetY()).thenReturn(-10);
//        for (int cycles = 0; cycles < 100; ++cycles ) {
//            testShipController.updateStatus(mockTouchEventResult);
//        }
//
//        verify(mockGameShip, times(100)).setY(any(Integer.class));
//    }
}