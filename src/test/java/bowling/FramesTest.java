package bowling;

import bowling.domain.frame.Frame;
import bowling.domain.frame.Frames;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class FramesTest {
    @DisplayName("프레임의 결과가 Gutter 또는 Miss 일 때, 프레임의 점수를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"0:0", "8:1", "5:4"}, delimiter = ':')
    void returnFrameScoreWhenGutterOrMiss(int firstPoint, int secondPoint) {
        //given
        Frame frame = new Frame(0, firstPoint, secondPoint);
        Frames frames = new Frames(Arrays.asList(frame));

        //when
        int frameScore = frames.getFrameScore(frame.getFrameId());

        //then
        assertThat(frameScore)
                .isEqualTo(firstPoint + secondPoint);
    }

    @DisplayName("프레임의 결과가 Spare일 때, 프레임의 점수는 다음 투구의 첫 번째 포인트를 합산해 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"9:1", "8:2", "0:10"}, delimiter = ':')
    void returnFrameScoreWhenSpare(int firstPoint, int secondPoint) {
        //given
        Frame frame = new Frame(0, firstPoint, secondPoint);
        Frame nextFrame = frame.createNextFrame();
        Frames frames = new Frames(Arrays.asList(frame, nextFrame));

        //when
        int frameScore = frames.getFrameScore(frame.getFrameId());

        //then
        assertThat(frameScore)
                .isEqualTo(firstPoint + secondPoint + nextFrame.getFirstPoint());
    }

    @DisplayName("프레임의 결과가 Strike이고 다음 투구의 첫 포인트가 Strike가 아닐 때,프레임의 점수를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"8:2", "7:2", "0:0"}, delimiter = ':')
    void returnFrameScoreWhenStrikeAndNextFirstIsNotStrike(int nextFirstPoint, int nextSecondPoint) {
        //given
        Frame frame = new Frame(0, 10, 0);
        Frame nextFrame = new Frame(1, nextFirstPoint, nextSecondPoint);
        Frames frames = new Frames(Arrays.asList(frame, nextFrame));

        //when
        int frameScore = frames.getFrameScore(frame.getFrameId());

        //then
        assertThat(frameScore)
                .isEqualTo(10 + nextFirstPoint + nextSecondPoint);
    }

    @DisplayName("프레임의 결과가 Strike이고 다음 투구의 첫 포인트가 Strike일 때,프레임의 점수를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"10:0", "8:2", "7:2", "0:0"}, delimiter = ':')
    void returnFrameScoreWhenStrikeAndNextFirstIsStrike(int firstOfThirdFrame, int secondOfThirdFrame) {
        //given
        Frame firstFrame = new Frame(0, 10, 0);
        Frame secondFrame = new Frame(1, 10, 0);
        Frame thirdFrame = new Frame(2, firstOfThirdFrame, secondOfThirdFrame);

        Frames frames = new Frames(Arrays.asList(firstFrame, secondFrame, thirdFrame));

        //when
        int frameScore = frames.getFrameScore(firstFrame.getFrameId());

        //then
        assertThat(frameScore)
                .isEqualTo(10 + 10 + firstOfThirdFrame);
    }

    @DisplayName("10회 첫 투구가 Strike일 때, 프레임의 점수를 반환한다.")
    @Test
    void returnFrameScoreWhenTenthIsStrike() {
        //given
        Frame tenthFrame = new Frame(9, 10, 0);
        Frames framesForResult = new Frames(createTenFramesWithOneFrame(tenthFrame));

        //when
        int frameScore = framesForResult.getFrameScore(tenthFrame.getFrameId());

        //then
        assertThat(frameScore)
                .isEqualTo(10 + tenthFrame.getThirdPoint() + tenthFrame.getFourthPoint());
    }

    @DisplayName("10회 투구가 Spare일 때, 프레임의 점수를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"9:1", "8:2", "0:10"}, delimiter = ':')
    void returnFrameScoreWhenTenthIsSpare(int firstPoint, int secondPoint) {
        //given
        Frame tenthFrame = new Frame(9, firstPoint, secondPoint);
        Frames framesForResult = new Frames(createTenFramesWithOneFrame(tenthFrame));

        //when
        int frameScore = framesForResult.getFrameScore(tenthFrame.getFrameId());

        //then
        assertThat(frameScore)
                .isEqualTo(firstPoint + secondPoint + tenthFrame.getThirdPoint());
    }

    @DisplayName("10회 투구가 Miss 또는 Gutter 일 때, 프레임의 점수를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"0:0", "8:1", "5:4"}, delimiter = ':')
    void returnFrameScoreWhenTenthIsGutterOrMiss(int firstPoint, int secondPoint) {
        //given
        Frame tenthFrame = new Frame(9, firstPoint, secondPoint);
        Frames framesForResult = new Frames(createTenFramesWithOneFrame(tenthFrame));

        //when
        int frameScore = framesForResult.getFrameScore(tenthFrame.getFrameId());

        //then
        assertThat(frameScore)
                .isEqualTo(tenthFrame.getFirstPoint() + tenthFrame.getSecondPoint());
    }

    @DisplayName("모든 프레임이 스트라이크일 때, 총점은 300점을 반환한다.")
    @Test
    void returnSumPointsWhenAllStrike() {
        //given
        Frames frames = new Frames(createTenStrikeFrames());

        //when
        int totalPointUntil = frames.getTotalPointUntil(frames.getFrames().get(9));

        //then
        assertThat(totalPointUntil).isEqualTo(300);
    }

    private List<Frame> createTenStrikeFrames() {
        List<Frame> frames = new ArrayList<>();
        IntStream.range(0, 9)
                .forEach(it -> frames.add(new Frame(it, 10, 0)));
        frames.add(new Frame(9, 10, 0, 10, 10));
        return frames;
    }

    private List<Frame> createTenFramesWithOneFrame(Frame frame) {
        List<Frame> frames = new ArrayList<>();
        IntStream.range(0, 10)
                .forEach(it -> frames.add(frame));

        return frames;
    }
}