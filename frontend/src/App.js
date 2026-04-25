import React, { useState } from "react";
import "./App.css";

const API = "http://localhost:8080/api";

function App() {
    const [user, setUser] = useState(null);
    const [role, setRole] = useState("");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [balance, setBalance] = useState(null);
    const [toUser, setToUser] = useState("");
    const [amount, setAmount] = useState("");
    const [message, setMessage] = useState("");

    const [allUsers, setAllUsers] = useState([]);

    const register = async () => {
        const res = await fetch(`${API}/register`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, password })
        });

        const data = await res.json();
        setMessage(data.message);
    };

    const login = async () => {
        setUser(null);
        setRole("");

        const res = await fetch(`${API}/login`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, password })
        });

        const data = await res.json();

        if (data.success === true) {
            setUser(username);
            setRole(data.role);
            setBalance(data.balance);
            setMessage("");
        } else {
            setMessage(data.message);
        }
    };

    const logout = () => {
        setUser(null);
        setRole("");
        setUsername("");
        setPassword("");
        setBalance(null);
        setMessage("");
        setToUser("");
        setAmount("");
        setAllUsers([]);
    };

    const getBalance = async () => {
        const res = await fetch(`${API}/balance/${user}`);
        const data = await res.json();

        if (data.success) {
            setBalance(data.balance);
            setRole(data.role);
        } else {
            setMessage(data.message);
        }
    };

    const transfer = async () => {
        const res = await fetch(`${API}/transfer`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                from: user,
                to: toUser,
                amount: parseFloat(amount)
            })
        });

        const data = await res.json();
        setMessage(data.message);

        if (data.success) {
            setBalance(data.balance);
            setAmount("");
            setToUser("");
        }
    };
    const fetchAllUsers = async () => {
        try {
            const res = await fetch(`${API}/admin/users`);
            const data = await res.json();
            setAllUsers(data);
        } catch (err) {
            setMessage("Ошибка доступа к админ-панели");
        }
    };

    return (
        <div className="container">
            <h1>💳 Cloud Bank</h1>

            {!user && (
                <>
                    <h2>Login / Register</h2>
                    <input
                        placeholder="Username"
                        value={username}
                        onChange={e => setUsername(e.target.value)}
                    />
                    <input
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={e => setPassword(e.target.value)}
                    />
                    <div style={{ display: "flex", gap: "10px" }}>
                        <button onClick={login}>Login</button>
                        <button onClick={register} className="button-secondary">Register</button>
                    </div>
                    {message && <div className="message">{message}</div>}
                </>
            )}

            {user && (
                <>
                    <div className="balance-box">
                        Welcome, {user}! <br/>
                        <span style={{fontSize: "12px", color: "#666"}}>Role: {role}</span>
                    </div>

                    <button onClick={logout} style={{marginTop: "10px", background: "#d32f2f"}}>Logout</button>
                    {role === "ADMIN" && (
                        <div className="section" style={{ border: "2px solid #2a5298", padding: "15px", borderRadius: "10px", background: "#eef2f7" }}>
                            <h3 style={{ color: "#2a5298", marginTop: 0 }}>🛡️ Admin Panel</h3>
                            <p style={{fontSize: "13px"}}>Доступно только для пользователей с ролью ADMIN</p>
                            <button onClick={fetchAllUsers}>Get All Users Data</button>

                            {allUsers.length > 0 && (
                                <ul style={{ textAlign: "left", fontSize: "14px", marginTop: "10px" }}>
                                    {allUsers.map(u => (
                                        <li key={u.id}>
                                            <b>{u.username}</b>: ${u.balance} ({u.role})
                                        </li>
                                    ))}
                                </ul>
                            )}
                        </div>
                    )}

                    <div className="section">
                        <h3>Your Account</h3>
                        <button onClick={getBalance}>Refresh Balance</button>
                        {balance !== null && (
                            <div className="balance-box" style={{background: "#fff", border: "1px solid #ddd"}}>
                                Current Balance: ${balance}
                            </div>
                        )}
                    </div>

                    <div className="section">
                        <h3>Transfer Money</h3>
                        <input
                            placeholder="Recipient username"
                            value={toUser}
                            onChange={e => setToUser(e.target.value)}
                        />
                        <input
                            placeholder="Amount"
                            value={amount}
                            onChange={e => setAmount(e.target.value)}
                        />
                        <button onClick={transfer}>Send Money</button>
                        {message && <div className="message">{message}</div>}
                    </div>
                </>
            )}
        </div>
    );
}

export default App;