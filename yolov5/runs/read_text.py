# detecting한 값 저장한 파일에서 라벨 읽어오기
with open("./save_detecting_label_folder/ex.txt", "r") as f:
    for line in f:
        result = line.strip()

print(result)