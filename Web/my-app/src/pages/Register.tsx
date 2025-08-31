import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { auth } from '../firebase'
import { createUserWithEmailAndPassword, updateProfile } from 'firebase/auth'

export default function Register() {
    const navigate = useNavigate()
    const [name, setName] = useState('')
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState<string | null>(null)


    const backendUrl = import.meta.env.VITE_BACKEND_URL as string


    const handleRegister = async (e: React.FormEvent) => {
        e.preventDefault()
        setError(null)
        setLoading(true)


        try {
            const cred = await createUserWithEmailAndPassword(auth, email, password)
            if (name) {
                await updateProfile(cred.user, { displayName: name })
            }


            const idToken = await cred.user.getIdToken()

            // Salvar no backend (Firestore) com role STUDENT/ALUNO
            await fetch(`${backendUrl}/users`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${idToken}`,
                },
                body: JSON.stringify({
                    uid: cred.user.uid,
                    name: name || cred.user.displayName,
                    email: cred.user.email,
                    role: 'STUDENT',
                }),
            })


            navigate('/dashboard')
        } catch (e: any) {
            setError(e.message || 'Erro ao cadastrar')
        } finally {
            setLoading(false)
        }
    }

    return (
        <div style={styles.container}>
            <div style={styles.card}>
                <h1 style={styles.title}>Cadastro (Alunos)</h1>
                <form onSubmit={handleRegister}>
                    <input
                        style={styles.input}
                        placeholder="Nome completo"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                    />
                    <input
                        style={styles.input}
                        type="email"
                        placeholder="Email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                    <input
                        style={styles.input}
                        type="password"
                        placeholder="Senha"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                    <button style={styles.submit} type="submit" disabled={loading}>
                        {loading ? 'Enviando…' : 'Criar conta'}
                    </button>
                </form>


                {error && <p style={styles.error}>{error}</p>}


                <p style={styles.helper}>
                    Já tem conta? <Link to="/login">Entrar</Link>
                </p>
            </div>
        </div>
    )
}

const styles: Record<string, React.CSSProperties> = {
    container: { display: 'flex', alignItems: 'center', justifyContent: 'center', minHeight: '100vh', background: '#f6f7fb' },
    card: { width: 380, background: '#fff', padding: 24, borderRadius: 16, boxShadow: '0 10px 30px rgba(0,0,0,0.08)' },
    title: { marginBottom: 16, textAlign: 'center' },
    input: { width: '100%', padding: '12px 14px', marginBottom: 12, borderRadius: 10, border: '1px solid #ddd' },
    submit: { width: '100%', padding: '12px 16px', borderRadius: 12, border: 'none', background: '#111827', color: '#fff', fontWeight: 600, cursor: 'pointer' },
    error: { color: '#c0392b', marginTop: 12 },
    helper: { fontSize: 14, marginTop: 16, textAlign: 'center' },
}
