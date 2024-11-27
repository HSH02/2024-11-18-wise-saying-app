package wise.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {

    /**
     * 파일 이름과 파일 내용이 예상 값과 일치하는지 검증
     *
     * @param filePath       파일 경로
     * @param expectedName   예상 파일 이름
     * @param expectedContent 예상 파일 내용
     * @return true: 파일 이름과 내용이 모두 일치, false: 그렇지 않음
     * @throws IOException 파일 읽기 실패 시
     */
    public static boolean isFileValid(String filePath, String expectedName, String expectedContent) throws IOException {
        File file = new File(filePath + expectedName);

        // 파일 존재 여부 확인
        if (!file.exists()) {
            return false;
        }

        // 파일 이름 확인
        if (!file.getName().equals(expectedName)) {
            return false;
        }

        // 파일 내용 읽기
        StringBuilder fileContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line);
            }
        }

        // 파일 내용 확인 (줄바꿈 제거 후 비교)
        return fileContent.toString().trim().equals(expectedContent.trim());
    }
}
