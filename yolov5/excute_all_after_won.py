import os.path
import cv2 as cv
import subprocess
import time
import firebase_admin
from firebase_admin import credentials, storage
from firebase_admin import db
from firebase_admin import storage

# storage 인증 키
cred = credentials.Certificate("storage.json")
firebase_admin.initialize_app(cred,{"storageBucket": "aicd-fd750.appspot.com"})

# 실시간으로 값이 들어오는지 확인하고 들어오면 디텍트를 실행하기 위한 무한 루프
bucket = storage.bucket()

cmd = "python3 ../openai/flask_ex.py"
bashCmd = cmd.split()
process = subprocess.Popen(bashCmd)

while True:
    print("----------- 제발 되주세요-------------")

    # 안드로이드에서 업로드시 확장자 안붙어서 나옴 - 그럴땐 밑에 코드
    
    # # 원래 경로 - 스토리지 images 폴더안에 이미지 존재하는지 체크하기위한 경로
    folder_path = 'images/image'
    blob_for_mohan = bucket.blob(folder_path)
    exists = blob_for_mohan.exists()

    # 변경 후 경로 - 안드로이드에서 업로드한 이미지가 IMAGE로 저장된다는 전재하에


    # 이미지 존재 여부 확인 - 직접 파일 업로드시 확장자가 붙어나옴
    # 가져올 이미지가 있는 폴더 경로
    # folder_path = 'images/'

    # blobs = bucket.list_blobs(prefix=folder_path)
    # image_exists = False
    # for blob in blobs:
    #     if blob.name.endswith(('.jpg', '.jpeg', '.png', '.gif')):
    #         image_exists = True
    #         break
    
    # 안드로이드에서 IMAGE라고 업로드시 image_exists를 exists라고 변경
    if exists:
        # 폴더 내의 모든 파일 가져오기
        blobs = bucket.list_blobs(prefix=folder_path)

        # 이미지 파일들 다운로드
        for blob in blobs:
            if blob.content_type.startswith('image/'):
                # 확장자 안맞으면
                # destination_file = f"ex/{blob.name.split('/')[-1]}"
                destination_file = f"ex_for_detect_img/{blob.name.split('/')[-1]}"
                print(blob.name)
                
                # 안드로이드에서 업로드시 IMAGE라고 올라화서 사진 확장자 추가해줌 - detect시 확장자 없으면 실행 안됨
                blob.download_to_filename(destination_file+".jpeg")

                # 파이어베이스에 직접 파일 업로드하여 테스트 할 때
                # blob.download_to_filename(destination_file)

                print(f"Downloaded: {destination_file}")
                blob.delete()
                print("delete")
            elif blob.content_type.startswith('image/') != True:
                print("non")

        file = './ex_for_detect_img/'
        # 사용 예시
        if os.listdir(file):
            img_path = os.listdir(file)
            img_path = img_path[0]
            print(img_path)
            global for_dect_img
            for_dect_img = file + img_path
            print("for",for_dect_img)

        # subprocess 모듈은 파이썬에서 쉘 명령을 실행할 수 있게 해주는 라이브러리이다.
            cmd = f"python3 detect.py --weights ./runs/train/ex/best.pt --img 416 --conf 0.3 --source {for_dect_img}"
            bashCmd = cmd.split()
            process = subprocess.Popen(bashCmd)
            time.sleep(10)
            os.remove(for_dect_img)
            process.terminate()
    else:
        print("스토리지에 사진이없어용")
        time.sleep(0.05)
        #print("아직도 없어용?")
        #time.sleep(1)