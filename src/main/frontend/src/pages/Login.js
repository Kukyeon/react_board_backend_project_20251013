import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Signup.css";
import api from "../api/axiosConfig";

function Login({ onLogin }){

    const [username , setUsername] = useState("");
    const [password , setPassword] = useState("");
    const navigate = useNavigate();

    const handleLogin = async(e) => {
        e.preventDefault(); // submit 이벤트 후 초기화 방지
        try {
            //로그인 요청(폼 데이터 방식)
            await api.post("/api/auth/login",
                new URLSearchParams({username, password})
            );

            //현재 로그인한 사용자 정보 가져오기
            const res = await api.get("api/auth/me");
            onLogin(res.data.username); 
            //App에서 전달된 props인 onLogin의 값으로 로그인한 유저의 username 전달

            alert("로그인 성공");
            navigate("/", {replace:true});
        } catch (err) {
            console.error(err);
            alert("로그인 실패!");
        }
    }

    return(
        <div className="form-container">
            <h2>회원로그인</h2>
            <form onSubmit={handleLogin}>
                <input type="text" placeholder="아이디" value={username} 
                onChange={(e) => setUsername(e.target.value)} /> <br />
                <input type="password" placeholder="비밀번호" value={password} 
                onChange={(e) => setPassword(e.target.value)} /> <br /><br />
                <button type="submit">로그인</button>
            </form>
            
        </div>
    );
}

export default Login;