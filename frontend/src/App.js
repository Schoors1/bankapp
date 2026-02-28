import React, { useState } from "react";
import "./App.css";

const API = "http://localhost:8080/api";

function App() {
    const [user, setUser] = useState(null);
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [balance, setBalance] = useState(null);
    const [toUser, setToUser] = useState("");
    const [amount, setAmount] = useState("");
    const [message, setMessage] = useState("");

    const register = async () => {
        const res = await fetch(`${API}/register`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, password })
        });

        const data = await res.text();
        setMessage(data);
    };

    const login = async () => {
        const res = await fetch(`${API}/login`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, password })
        });

        const data = await res.text();

        if (res.ok) {
            setUser(username);
            setMessage("");
        } else {
            setMessage(data);
        }
    };

    const logout = () => {
        setUser(null);
        setUsername("");
        setPassword("");
        setBalance(null);
        setMessage("");
    };

    const getBalance = async () => {
        const res = await fetch(`${API}/balance/${user}`);
        const data = await res.json();
        setBalance(data.balance);
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

        const data = await res.text();
        setMessage(data);
    };

    return (
        <div className="container">
            <h1>💳 Simple Bank</h1>

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
                        <button onClick={register} className="button-secondary">
                            Register
                        </button>
                    </div>

                    {message && <div className="message">{message}</div>}
                </>
            )}

            {user && (
                <>
                    <h2>Welcome, {user}</h2>

                    <button
                        onClick={logout}
                        className="button-secondary"
                        style={{ width: "100%", marginBottom: "15px" }}
                    >
                        Logout
                    </button>

                    <div className="section">
                        <h3>Balance</h3>
                        <button onClick={getBalance}>Check Balance</button>

                        {balance !== null && (
                            <div className="balance-box">
                                Balance: ${balance}
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

                        <button onClick={transfer} style={{ width: "100%" }}>
                            Send Money
                        </button>

                        {message && <div className="message">{message}</div>}
                    </div>
                </>
            )}
        </div>
    );
}

export default App;