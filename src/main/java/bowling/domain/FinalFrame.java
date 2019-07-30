package bowling.domain;

import bowling.domain.state.InitState;
import bowling.domain.state.State;
import bowling.exception.OutOfBowlCountException;

/**
 * author       : gwonbyeong-yun <sksggg123>
 * ------------------------------------------
 * | email        : sksggg123               |
 * | github       : github.com/sksggg123    |
 * | blog         : sksggg123.github.io     |
 * ------------------------------------------
 * project      : java-bowling
 * create date  : 2019-07-19 15:29
 */
public class FinalFrame implements Frame {
    public static final int LAST_FRAME_NUMBER = 10;
    private State state;
    private final FrameNumber frameNumber;

    public FinalFrame(FrameNumber frameNumber) {
        this.state = InitState.of();
        this.frameNumber = frameNumber;
    }

    @Override
    public Frame bowl(int fallCount) {
        if (isGameOver()) {
            throw new OutOfBowlCountException();
        }
        state = state.update(Point.of(fallCount), true);
        return this;
    }

    @Override
    public boolean isGameOver() {
        return state.isOver(true);
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public int getNumber() {
        return frameNumber.getFrameNumber();
    }

    @Override
    public Score getScore() {
        return state.stateScore();
    }

    @Override
    public Score updateScore(Score source) {
        return source;
    }

    @Override
    public String toString() {
        return "FinalFrame{" +
                "state=" + state +
                ", frameNumber=" + frameNumber +
                '}';
    }
}