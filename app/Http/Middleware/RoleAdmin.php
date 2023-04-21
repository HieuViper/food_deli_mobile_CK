<?php

namespace App\Http\Middleware;

use Closure;
use Illuminate\Http\Request;

class RoleAdmin
{

    public function handle(Request $request, Closure $next)
    {
        if (auth()->check() && auth()->user()->type == "admin")
            return $next($request);
        abort(403);
    }
}
