import { useState } from "react";
import "./Signup.css";
//import axios from "axios";
import { useNavigate } from "react-router-dom";
import api from "../api/axiosConfig";


function Signup() {

    const [username , setUsername] = useState("");
    const [password , setPassword] = useState("");
    const [errors, setErrors] = useState({});
    const navigate = useNavigate();

    const handleSignup = async(e) => {
        e.preventDefault();
        try {
            await api.post("/api/auth/signup",{username, password});
            alert("회원가입 성공!");
            navigate("/login");
        } catch (err) {
            if(err.response && err.response.status === 400) {
                setErrors(err.response.data);
            }else{
                console.error("회원가입실패:",err);
                alert("회원가입 실패!");
            }
            
        }
    }

    return (
        <div className="form-container">
            <h2>회원가입</h2>
            <form onSubmit={handleSignup}>
                <input type="text" placeholder="아이디" value={username} 
                onChange={(e) => setUsername(e.target.value)} /> <br />
                
                <input type="password" placeholder="비밀번호" value={password} 
                onChange={(e) => setPassword(e.target.value)} /> <br /><br />
                {errors.username && <p style={{color:"red"}}>{errors.username}</p>}
                {errors.password && <p style={{color:"red"}}>{errors.password}</p>}
                {errors.iderror && <p style={{color:"red"}}>{errors.iderror}</p>}
                <button type="submit">회원가입</button>
            </form>
            
        </div>
    );
}

export default Signup;