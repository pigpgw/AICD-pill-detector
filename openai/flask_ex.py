from flask import Flask, render_template, request
import sys
import os
import openai

web_gui = Flask(__name__)
@web_gui.route('/')
def index():
    return render_template('index.html')

@web_gui.route('/create/', methods = ['GET','POST'])
def user_input():
    # 입력 받아오기
    if request.method == 'GET':
        return render_template('index.html')
    # 받은값 openai에 넣어서 출력
    elif request.method == 'POST':
        body = request.form['body']
        user = body
        openai.api_key = "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"

        text = f"""From now on, you can solve the patient's questions as a doctor."""
        prompt = f"""
        {user} 
        ```{text}```
        """

        response = openai.Completion.create(
        model="text-davinci-003",
        prompt= prompt,
        temperature=0, # 0에 가까울수록 정확한 값을 추출 1에 가까울수록 창의적
        max_tokens=4000
        )

        print("-------------------------------------")
        body = response["choices"][0]["text"].strip()
        print("-------------------------------------")

    return render_template('for_user.html',content=body)

# 두번째 경로를 보내주면 외부에서 접속가능
if __name__ == '__main__':
    web_gui.run(host='0.0.0.0',debug=True, port=9999)
