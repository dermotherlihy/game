package com.dermotherlihy.game.domain;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.CountDownLatch;

/**
 * Created by dermot.herlihy on 27/01/2016.
 */
public class RefereeTest {

    @Mock
    private Player playerMock;

    @Mock
    private Game gameMock;

    @Mock
    private CountDownLatch whistleMock;


    private Referee testObj = new Referee(whistleMock);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

    }

}
